package cz.smartbrains.qesu.module.finance.service

import cz.smartbrains.qesu.module.invoice.repository.InvoiceRepository
import cz.smartbrains.qesu.module.invoice.type.InvoiceType
import cz.smartbrains.qesu.module.finance.dto.AmountSummaryDto
import cz.smartbrains.qesu.module.moneyBox.repository.MoneyMovementRepository
import cz.smartbrains.qesu.module.moneyBox.type.MoneyMovementType
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate

@Service
@Transactional(readOnly = true)
class FinanceServiceImpl(private val invoiceRepository: InvoiceRepository, private val movementRepository: MoneyMovementRepository) :
    FinanceService {
    override fun vatSummary(dateFrom: LocalDate, dateTo: LocalDate, currency: String): AmountSummaryDto {
        val invoiceOutcomeVatSum = invoiceRepository.sumVat(dateFrom, dateTo, currency, InvoiceType.OUTCOME).orElse(BigDecimal.ZERO)
        val invoiceIncomeVatSum = invoiceRepository.sumVat(dateFrom, dateTo, currency, InvoiceType.INCOME).orElse(BigDecimal.ZERO)
        val incomeWithoutInvoiceSum = movementRepository.sumVatNoInvoice(dateFrom, dateTo, MoneyMovementType.INCOME, currency).orElse(BigDecimal.ZERO)
        //outcome movements have negative amount/vat
        val outcomeWithoutInvoiceSum = movementRepository.sumVatNoInvoice(dateFrom, dateTo, MoneyMovementType.OUTCOME, currency).orElse(BigDecimal.ZERO)
        return AmountSummaryDto(dateFrom, dateTo, invoiceOutcomeVatSum!!.subtract(invoiceIncomeVatSum).add(incomeWithoutInvoiceSum).add(outcomeWithoutInvoiceSum))
    }

    override fun taxBaseSummaryByYear(dateFrom: LocalDate, dateTo: LocalDate, currency: String): AmountSummaryDto {
        val invoiceSum = invoiceRepository.sumAmount(dateFrom, dateTo, currency).orElse(BigDecimal.ZERO)
        val incomeWithoutInvoiceSum = movementRepository.sumAmountNoInvoice(dateFrom, dateTo, MoneyMovementType.INCOME, currency).orElse(BigDecimal.ZERO)

        //outcome movements have negative amount/vat
        val outcomeWithoutInvoiceSum = movementRepository.sumAmount(dateFrom, dateTo, MoneyMovementType.OUTCOME, currency).orElse(BigDecimal.ZERO)
        return AmountSummaryDto(dateFrom, dateTo, invoiceSum!!.add(incomeWithoutInvoiceSum).add(outcomeWithoutInvoiceSum))
    }
}