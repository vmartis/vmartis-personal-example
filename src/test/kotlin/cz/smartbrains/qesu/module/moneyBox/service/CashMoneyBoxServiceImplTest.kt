package cz.smartbrains.qesu.module.moneyBox.service

import cz.smartbrains.qesu.module.common.service.OrderableEntityService
import cz.smartbrains.qesu.module.moneyBox.entity.CashMoneyBox
import cz.smartbrains.qesu.module.moneyBox.entity.MoneyBoxBalance
import cz.smartbrains.qesu.module.moneyBox.mapper.CashMoneyBoxMapper
import cz.smartbrains.qesu.module.moneyBox.repository.CashMoneyBoxRepository
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
class CashMoneyBoxServiceImplTest {
    @Mock
    private val cashMoneyBoxMapper: CashMoneyBoxMapper? = null
    @Mock
    private val cashMoneyBoxRepository: CashMoneyBoxRepository? = null
    @Mock
    private val moneyBoxRepository: MoneyBoxRepository? = null
    @Mock
    private val orderableEntityService: OrderableEntityService? = null

    private var service: CashMoneyBoxService? = null
    @BeforeEach
    fun setUp() {
        service = CashMoneyBoxServiceImpl(cashMoneyBoxMapper!!, cashMoneyBoxRepository!!, moneyBoxRepository!!, orderableEntityService!!)
    }

    @Test
    fun updateAccount_allValuesAreUpdated() {
        val boxId = 1L
        val box = CashMoneyBox()
        val totalAmount = BigDecimal.valueOf(1000)
        val amountCash = BigDecimal.valueOf(300)
        val amountCheque = BigDecimal.valueOf(350)
        Mockito.`when`(cashMoneyBoxRepository!!.computeBalance(boxId)).thenReturn(MoneyBoxBalance(totalAmount,
                amountCash,
                amountCheque))
        Mockito.`when`(cashMoneyBoxRepository.getById(boxId)).thenReturn(box)
        service!!.updateAccount(boxId)
        Assertions.assertThat(box.amount).isEqualTo(totalAmount)
        Assertions.assertThat(box.amountCash).isEqualTo(amountCash)
        Assertions.assertThat(box.amountCheque).isEqualTo(amountCheque)
    }
}