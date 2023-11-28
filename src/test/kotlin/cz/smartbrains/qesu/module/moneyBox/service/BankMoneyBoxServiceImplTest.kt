package cz.smartbrains.qesu.module.moneyBox.service

import cz.smartbrains.qesu.module.bank.repository.BankAccountRepository
import cz.smartbrains.qesu.module.bank.service.BankAccountService
import cz.smartbrains.qesu.module.common.service.OrderableEntityService
import cz.smartbrains.qesu.module.company.repository.CompanyBranchRepository
import cz.smartbrains.qesu.module.moneyBox.entity.BankMoneyBox
import cz.smartbrains.qesu.module.moneyBox.entity.MoneyBoxBalance
import cz.smartbrains.qesu.module.moneyBox.mapper.BankMoneyBoxMapper
import cz.smartbrains.qesu.module.moneyBox.repository.BankMoneyBoxRepository
import cz.smartbrains.qesu.module.moneyBox.repository.MoneyBoxRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal

@ExtendWith(MockitoExtension::class)
class BankMoneyBoxServiceImplTest {
    private var service: BankMoneyBoxServiceImpl? = null

    @Mock
    private val mapper: BankMoneyBoxMapper? = null

    @Mock
    private val bankMoneyBoxRepository: BankMoneyBoxRepository? = null

    @Mock
    private val moneyBoxRepository: MoneyBoxRepository? = null

    @Mock
    private val companyBranchRepository: CompanyBranchRepository? = null

    @Mock
    private val bankAccountService: BankAccountService? = null

    @Mock
    private val bankAccountRepository: BankAccountRepository? = null

    @Mock
    private val orderableEntityService: OrderableEntityService? = null

    @BeforeEach
    fun setUp() {
        service = BankMoneyBoxServiceImpl(
                mapper!!,
                bankMoneyBoxRepository!!,
                moneyBoxRepository!!,
                companyBranchRepository!!,
                bankAccountService!!,
                bankAccountRepository!!,
                orderableEntityService!!)
    }

    @Test
    fun updateAccount_correctComputeAmount() {
        val moneyBoxId = 1L
        val moneyBox = BankMoneyBox()
        moneyBox.initialAmount = BigDecimal.valueOf(333)
        Mockito.`when`(bankMoneyBoxRepository!!.computeBalance(moneyBoxId)).thenReturn(MoneyBoxBalance(BigDecimal.valueOf(1500)))
        Mockito.`when`(bankMoneyBoxRepository.getById(moneyBoxId)).thenReturn(moneyBox)
        service!!.updateAccount(moneyBoxId)
        Assertions.assertThat(moneyBox.amount).isEqualTo(BigDecimal.valueOf(1833))
    }
}