package cz.smartbrains.qesu.module.moneyBox.service

import com.google.common.collect.Lists
import cz.smartbrains.qesu.module.bank.entity.BankAccount
import cz.smartbrains.qesu.module.bank.entity.BankTransaction
import cz.smartbrains.qesu.module.bank.mapper.BankTransactionMapper
import cz.smartbrains.qesu.module.bank.repository.BankAccountRepository
import cz.smartbrains.qesu.module.bank.repository.BankTransactionRepository
import cz.smartbrains.qesu.module.bank.service.BankAccountService
import cz.smartbrains.qesu.module.common.exception.RecordNotFoundException
import cz.smartbrains.qesu.module.moneyBox.entity.BankMoneyMovement
import cz.smartbrains.qesu.module.moneyBox.repository.BankMoneyBoxRepository
import cz.smartbrains.qesu.module.moneyBox.repository.BankMoneyMovementRepository
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.function.Consumer

@Service
class BankMoneyMovementSyncServiceImpl(private val bankTransactionRepository: BankTransactionRepository,
                                       private val bankTransactionMapper: BankTransactionMapper,
                                       private val bankMoneyMovementRepository: BankMoneyMovementRepository,
                                       private val bankMoneyBoxService: BankMoneyBoxService,
                                       private val bankMoneyBoxRepository: BankMoneyBoxRepository,
                                       private val bankAccountService: BankAccountService,
                                       private val bankAccountRepository: BankAccountRepository) : BankMoneyMovementSyncService {
    private val log = LoggerFactory.getLogger(javaClass)

    @Transactional
    override fun synchronize(bankAccountId: Long, user: AlfaUserDetails?) {
        var transactions: List<BankTransaction>
        val moneyBoxes: MutableSet<Long> = HashSet()
        val bankMoneyBox = bankMoneyBoxRepository.findByBankAccountId(bankAccountId).orElseThrow { RecordNotFoundException() }
        do {
            transactions = bankTransactionRepository.findAllUnmapped(bankMoneyBox.initialDate!!, SYNC_BATCH_SIZE)
            if (transactions.isNotEmpty()) {
                log.info("{} unmapped transaction  will be processed.", transactions.size)
                val movements: MutableList<BankMoneyMovement?> = Lists.newArrayList()
                for (transaction in transactions) {
                    val movement = bankTransactionMapper.doToMovement(transaction, user!!)!!
                    val bankAccountDto = bankAccountService.parseCorrespondingAccount(transaction.id!!)
                    bankAccountRepository.findByAccountIdAndBankId(bankAccountDto.accountId, bankAccountDto.bankId)
                            .ifPresent { correspondingBankAccount: BankAccount ->
                                movement.correspondingBankAccount = correspondingBankAccount
                                movement.subject = correspondingBankAccount.subject
                            }
                    movements.add(movement)
                    moneyBoxes.add(movement.moneyBox!!.id!!)
                }
                bankMoneyMovementRepository.saveAll(movements)
                bankMoneyMovementRepository.flush()
                moneyBoxes.forEach(Consumer { moneyBoxId: Long -> bankMoneyBoxService.updateAccount(moneyBoxId) })
            } else {
                log.info("No more unmapped transactions to be synchronized.")
            }
        } while (transactions.isNotEmpty())
    }

    companion object {
        private const val SYNC_BATCH_SIZE = 500
    }
}