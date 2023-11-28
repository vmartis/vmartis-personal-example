package cz.smartbrains.qesu.module.moneyBox.service

import cz.smartbrains.qesu.module.common.exception.RecordNotFoundException
import cz.smartbrains.qesu.module.bank.repository.BankAccountRepository
import cz.smartbrains.qesu.module.bank.service.BankAccountService
import cz.smartbrains.qesu.module.moneyBox.entity.BankMoneyMovement
import cz.smartbrains.qesu.module.moneyBox.repository.BankMoneyMovementRepository
import cz.smartbrains.qesu.module.subject.entity.Subject
import cz.smartbrains.qesu.module.subject.repository.SubjectRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.function.Consumer

@Service
class MoneyMovementSubjectPairServiceImpl(private val subjectRepository: SubjectRepository,
                                          private val bankAccountService: BankAccountService,
                                          private val bankAccountRepository: BankAccountRepository,
                                          private val bankMoneyMovementRepository: BankMoneyMovementRepository) : MoneyMovementSubjectPairService {
    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun pairSubject(movementId: Long, subjectId: Long?) {
        val movement = bankMoneyMovementRepository.findById(movementId).orElseThrow { RecordNotFoundException() }
        if (subjectId != null) {
            val subject = subjectRepository.findById(subjectId).orElseThrow { RecordNotFoundException() }
            if (movement.subject == null) {
                pairNotPairedMovement(movement, subject)
            } else if (movement.subject!!.id != subject.id) {
                changeMovementSubject(movement, subject)
            }
        } else if (movement.subject != null) {
            unpairSubject(movement)
        }
    }

    private fun pairNotPairedMovement(movement: BankMoneyMovement, subject: Subject) {
        log.info("Pair subject with id {} with bank money movement with id {}.", subject.id, movement.id)
        //no corresponding bank account, pair subject with direct relation
        if (movement.bankTransaction!!.correspondingAccountNumber == null) {
            log.info("Bank transaction has no corresponding bank account, subject will be paired directly.")
            movement.subject = subject
        } else {
            val correspondingBankAccount = bankAccountService.storeCorrespondingBankAccount(movement.bankTransaction!!.id!!, subject.id!!)
            val bankAccount = bankAccountRepository.getById(correspondingBankAccount.id!!)
            movement.correspondingBankAccount = bankAccount
            movement.subject = subject

            //pair all others movements with new bankAccount
            val unpaired = bankMoneyMovementRepository.findUnpairedByAccountNumber(bankAccount.accountId!!, bankAccount.bankId!!, bankAccount.iban)
            unpaired.forEach(Consumer { bankMoneyMovement: BankMoneyMovement ->
                bankMoneyMovement.correspondingBankAccount = bankAccount
                bankMoneyMovement.subject = subject
            })
            log.info("Paired bank moneybox movements: {}.", unpaired.size)
        }
    }

    private fun changeMovementSubject(movement: BankMoneyMovement, subject: Subject) {
        log.info("Change subject of bank money movement with id {}. New subject id {}.", movement.id, subject.id)
        //no corresponding bank account, pair subject with direct relation
        if (movement.correspondingBankAccount == null) {
            log.info("Bank transaction has no corresponding bank account, subject will be changed directly.")
            movement.subject = subject
        } else {
            val bankAccount = bankAccountRepository.getById(movement.correspondingBankAccount!!.id!!)
            bankAccount.subject = subject
            movement.subject = subject

            // change all movements with changed bankAccount
            val updated = bankMoneyMovementRepository.updateSubjectForCorrespondingAccount(bankAccount.id!!, subject)
            log.info("Changed bank movements: {}.", updated)
        }
    }

    /**
     * Movement was already paired, but now should be unpaired. Subject property and corresponding bank account need to
     * be set to null for this movement and all other movements with same corresponding bank account. If corresponding
     * bank account doesn't exist, only set subject property to null.
     * @param movement to be unpaired.
     */
    private fun unpairSubject(movement: BankMoneyMovement) {
        log.info("Unpair subject of bank money movement with id {}", movement.id)
        //no corresponding bank account, pair subject with direct relation
        if (movement.correspondingBankAccount == null) {
            log.info("Bank transaction has no corresponding bank account, subject will be unpaired directly.")
            movement.subject = null
        } else {
            val bankAccount = bankAccountRepository.getById(movement.correspondingBankAccount!!.id!!)
            movement.correspondingBankAccount = null
            movement.subject = null
            val unpaired = bankMoneyMovementRepository.unpairBankAccount(bankAccount.id!!)
            bankAccountRepository.deleteById(bankAccount.id!!)
            log.info("Changed bank movements: {}.", unpaired)
        }
    }
}