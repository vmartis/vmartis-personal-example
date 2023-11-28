package cz.smartbrains.qesu.module.takings.service

import cz.smartbrains.qesu.module.user.repository.UserRepository
import cz.smartbrains.qesu.module.common.exception.RecordNotFoundException
import cz.smartbrains.qesu.module.common.exception.ServiceRuntimeException
import cz.smartbrains.qesu.module.common.service.Messages
import cz.smartbrains.qesu.module.moneyBox.dto.CashMoneyMovementDto
import cz.smartbrains.qesu.module.moneyBox.repository.CashMoneyBoxRepository
import cz.smartbrains.qesu.module.moneyBox.service.CashMoneyMovementService
import cz.smartbrains.qesu.module.moneyBox.type.AccountingType
import cz.smartbrains.qesu.module.moneyBox.type.MoneyMovementType.Companion.of
import cz.smartbrains.qesu.module.takings.dto.TakingsDto
import cz.smartbrains.qesu.module.takings.dto.TakingsFilter
import cz.smartbrains.qesu.module.takings.dto.TakingsTransferDto
import cz.smartbrains.qesu.module.takings.entity.Takings
import cz.smartbrains.qesu.module.takings.mapper.TakingsMapper
import cz.smartbrains.qesu.module.takings.repository.TakingsRepository
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalTime
import java.util.stream.Collectors

@Transactional
@Service
class TakingsServiceImpl(private val repository: TakingsRepository,
                         private val cashMoneyBoxRepository: CashMoneyBoxRepository,
                         private val userRepository: UserRepository,
                         private val mapper: TakingsMapper,
                         private val cashMoneyMovementService: CashMoneyMovementService,
                         private val messages: Messages) : TakingsService {
    @Transactional(readOnly = true)
    override fun findBy(filter: TakingsFilter): List<TakingsDto> {
        return repository
                .findByFilter(filter)
                .stream()
                .map { entity: Takings -> mapper.doToDto(entity)!! }
                .collect(Collectors.toList())
    }

    override fun create(takingsDto: TakingsDto, user: AlfaUserDetails): TakingsDto {
        validateTakings(takingsDto)
        val entity = mapper.dtoToDo(takingsDto)
        entity!!.transferred = false
        entity.createdBy = userRepository.getById(user.id)
        repository.save(entity)
        return mapper.doToDto(entity)!!
    }

    override fun update(dto: TakingsDto, user: AlfaUserDetails): TakingsDto {
        val originalTakings = repository.findById(dto.id!!).orElseThrow { RecordNotFoundException() }
        if (originalTakings.transferred) {
            throw ServiceRuntimeException("takings.update.transferred")
        }
        validateTakings(dto)
        val takings = mapper.dtoToDo(dto)
        originalTakings.date = dto.date
        originalTakings.amountCash = dto.amountCash
        originalTakings.amountCheque = dto.amountCheque
        originalTakings.amountCard = dto.amountCard
        originalTakings.totalCashAmount = dto.totalCashAmount
        originalTakings.totalVatAmount = dto.totalVatAmount
        originalTakings.totalChequeAmount = dto.totalChequeAmount
        originalTakings.currency = takings!!.currency
        originalTakings.cashBox = takings.cashBox
        originalTakings.updatedBy = userRepository.getById(user.id)
        return dto
    }

    override fun transfer(transfer: TakingsTransferDto, user: AlfaUserDetails) {
        val originalTakings = repository.findById(transfer.takings!!.id!!).orElseThrow { RecordNotFoundException() }
        val moneyBox = cashMoneyBoxRepository.findById(transfer.moneyBox!!.id!!).orElseThrow { RecordNotFoundException() }
        if (originalTakings.transferred) {
            throw ServiceRuntimeException("takings.transfer.transferred")
        }
        if (originalTakings.currency != moneyBox.currency) {
            throw ServiceRuntimeException("takings.transfer.currency.invalid")
        }

        // create fiscal movement only if fiscal amount > 0
        val totalFiscal = originalTakings.totalFiscal
        if (totalFiscal.compareTo(BigDecimal.ZERO) > 0) {
            val movementFiscal = CashMoneyMovementDto()
            movementFiscal.amountCash = originalTakings.amountCash
            movementFiscal.amountCheque = originalTakings.amountCheque
            movementFiscal.accountingType = AccountingType.FISCAL
            movementFiscal.date = originalTakings.date!!.atTime(LocalTime.now())
            movementFiscal.taxableDate = originalTakings.date
            movementFiscal.moneyBox = transfer.moneyBox
            movementFiscal.note = messages.getMessage("takings.transfer.note",
                    originalTakings.cashBox!!.companyBranch!!.name,
                    originalTakings.cashBox!!.name)
            movementFiscal.totalVat = originalTakings.totalVatAmount
            movementFiscal.type = of(totalFiscal)
            movementFiscal.category = transfer.category
            cashMoneyMovementService.create(movementFiscal, user)
        }

        // create NOT fiscal movement only if not fiscal amount > 0
        val totalNonFiscal = originalTakings.totalNonFiscal
        if (totalNonFiscal.compareTo(BigDecimal.ZERO) > 0) {
            val movementNonFiscal = CashMoneyMovementDto()
            movementNonFiscal.amountCash = originalTakings.cashNonFiscal
            movementNonFiscal.amountCheque = originalTakings.chequeNonFiscal
            movementNonFiscal.accountingType = AccountingType.NON_FISCAL
            movementNonFiscal.date = originalTakings.date!!.atTime(LocalTime.now())
            movementNonFiscal.moneyBox = transfer.moneyBox
            movementNonFiscal.type = of(totalNonFiscal)
            movementNonFiscal.category = transfer.category
            movementNonFiscal.note = messages.getMessage("takings.transfer.note",
                    originalTakings.cashBox!!.companyBranch!!.name,
                    originalTakings.cashBox!!.name)
            cashMoneyMovementService.create(movementNonFiscal, user)
        }
        originalTakings.transferred = true
    }

    override fun delete(id: Long) {
        repository.deleteById(id)
    }

    private fun validateTakings(dto: TakingsDto) {
        if (dto.totalCashAmount.compareTo(dto.amountCash) < 0) {
            throw ServiceRuntimeException("takings.update.totalAmountCash.min")
        }
        if (dto.totalChequeAmount.compareTo(dto.amountCheque) < 0) {
            throw ServiceRuntimeException("takings.update.getTotalChequeAmount.min")
        }
    }
}