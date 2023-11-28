package cz.smartbrains.qesu.module.moneyBox.service

import cz.smartbrains.qesu.module.common.exception.RecordNotFoundException
import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.user.repository.UserRepository
import cz.smartbrains.qesu.module.moneyBox.dto.CashMoneyMovementDto
import cz.smartbrains.qesu.module.moneyBox.mapper.CashMoneyMovementMapper
import cz.smartbrains.qesu.module.moneyBox.repository.CashMoneyBoxRepository
import cz.smartbrains.qesu.module.moneyBox.repository.CashMoneyMovementRepository
import cz.smartbrains.qesu.module.moneyBox.type.MoneyMovementType.Companion.of
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CashMoneyMovementServiceImpl(private val cashMoneyMovementRepository: CashMoneyMovementRepository,
                                   private val cashMoneyMovementMapper: CashMoneyMovementMapper,
                                   private val cashMoneyBoxRepository: CashMoneyBoxRepository,
                                   private val cashMoneyBoxService: CashMoneyBoxService,
                                   private val userRepository: UserRepository,
                                   private val invoicePairService: MoneyMovementInvoicePairService) : CashMoneyMovementService {
    @Transactional
    override fun create(moneyMovement: CashMoneyMovementDto, user: AlfaUserDetails): CashMoneyMovementDto {
        val newMoneyMovement = cashMoneyMovementMapper.dtoToDo(moneyMovement)
        checkMoneyBox(moneyMovement.moneyBox!!.id!!)
        newMoneyMovement!!.type = of(newMoneyMovement.totalAmount!!)
        newMoneyMovement.active = true
        newMoneyMovement.createdBy = userRepository.getById(user.id)
        invoicePairService.payInvoice(moneyMovement)
        val movementPersisted = cashMoneyMovementRepository.save(newMoneyMovement)
        cashMoneyBoxService.updateAccount(newMoneyMovement.moneyBox!!.id!!)
        return cashMoneyMovementMapper.doToDto(movementPersisted)!!
    }

    @Transactional
    override fun update(moneyMovement: CashMoneyMovementDto, user: AlfaUserDetails): CashMoneyMovementDto {
        val originalMovement = cashMoneyMovementRepository.findById(moneyMovement.id!!).orElseThrow { RecordNotFoundException() }
        invoicePairService.payInvoice(moneyMovement, originalMovement)
        val newMovement = cashMoneyMovementMapper.dtoToDo(moneyMovement)
        originalMovement.type = newMovement!!.type
        originalMovement.invoice = newMovement.invoice
        originalMovement.subject = newMovement.subject
        originalMovement.category = newMovement.category
        originalMovement.totalVat = newMovement.totalVat
        originalMovement.accountingType = newMovement.accountingType
        originalMovement.amountCash = newMovement.amountCash
        originalMovement.amountCheque = newMovement.amountCheque
        originalMovement.note = newMovement.note
        originalMovement.totalAmount = newMovement.totalAmount
        originalMovement.totalVat = newMovement.totalVat
        originalMovement.totalWithoutVat = newMovement.totalWithoutVat
        originalMovement.note = newMovement.note
        originalMovement.active = newMovement.active
        originalMovement.date = newMovement.date
        originalMovement.taxableDate = newMovement.taxableDate
        originalMovement.updatedBy = userRepository.getById(user.id)
        cashMoneyBoxService.updateAccount(newMovement.moneyBox!!.id!!)
        return cashMoneyMovementMapper.doToDto(newMovement)!!
    }

    @Transactional
    override fun delete(id: Long) {
        val moneyMovement = cashMoneyMovementRepository.getById(id)
        invoicePairService.payInvoice(null, moneyMovement)
        cashMoneyMovementRepository.deleteById(id)
        cashMoneyBoxService.updateAccount(moneyMovement.moneyBox!!.id!!)
    }

    /**
     * Need to check existing of money box, because association between movement and box are generic and invalid type
     * of money box should be placed during create/update.
     * @param moneyBoxId id of money box to be checked
     */
    private fun checkMoneyBox(moneyBoxId: Long) {
        val moneyBoxExists = cashMoneyBoxRepository.existsById(moneyBoxId)
        if (!moneyBoxExists) {
            throw ServiceRuntimeException("moneyMovement.invalid.moneyBox")
        }
    }
}