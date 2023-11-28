package cz.smartbrains.qesu.module.order.mapper

import cz.smartbrains.qesu.module.company.mapper.CompanyBranchEntityMapper
import cz.smartbrains.qesu.module.company.mapper.CompanyEntityMapper
import cz.smartbrains.qesu.module.configuration.dto.ConfigurationDto
import cz.smartbrains.qesu.module.configuration.dto.InvoiceSettingDto
import cz.smartbrains.qesu.module.configuration.service.ConfigurationService
import cz.smartbrains.qesu.module.configuration.type.VatCalculationType
import cz.smartbrains.qesu.module.finance.service.FinanceCalculator
import cz.smartbrains.qesu.module.item.mapper.ItemEntityMapper
import cz.smartbrains.qesu.module.item.mapper.ItemMapper
import cz.smartbrains.qesu.module.order.dto.OrderDto
import cz.smartbrains.qesu.module.order.dto.OrderItemDto
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal

@ExtendWith(MockitoExtension::class)
class OrderMapperImplTest {
    @Mock
    private val itemMapper: ItemMapper? = null
    @Mock
    private val companyBranchEntityMapper: CompanyBranchEntityMapper? = null
    @Mock
    private val itemEntityMapper: ItemEntityMapper? = null
    @Mock
    private val companyEntityMapper: CompanyEntityMapper? = null
    @Mock
    private val financeCalculator: FinanceCalculator? = null
    @Mock
    private val configurationService: ConfigurationService? = null

    @InjectMocks
    private val mapper: OrderMapper = OrderMapperImpl()

    @BeforeEach
    fun setUp() {
        val configuration = ConfigurationDto()
        val invoiceSetting = InvoiceSettingDto()
        invoiceSetting.vatCalculationType = VatCalculationType.SUM
        configuration.invoice = invoiceSetting
        `when`(configurationService!!.find()).thenReturn(configuration)
    }

    @Test
    fun dtoToDo_calcTotalPriceAndTotalVat_isCorrect() {
        val item1 = OrderItemDto()
        item1.itemPrice = BigDecimal.valueOf(20)
        item1.ordered = BigDecimal.valueOf(5.5)
        item1.delivered = BigDecimal.valueOf(20)
        item1.vatRate = BigDecimal.valueOf(10)
        val item2 = OrderItemDto()
        item2.itemPrice = BigDecimal.valueOf(10)
        item2.ordered = BigDecimal.valueOf(3)
        item2.delivered = BigDecimal.valueOf(30)
        item2.vatRate = BigDecimal.valueOf(21)
        val orderDto = OrderDto()
        orderDto.items!!.add(item1)
        orderDto.items!!.add(item2)

        `when`(financeCalculator!!.calculateTotalWithVat(VatCalculationType.SUM, BigDecimal.valueOf(20), BigDecimal.valueOf(5.5), BigDecimal.valueOf(10)))
            .thenReturn(BigDecimal.valueOf(1))
        `when`(financeCalculator.calculateTotalWithVat(VatCalculationType.SUM, BigDecimal.valueOf(10), BigDecimal.valueOf(3), BigDecimal.valueOf(21)))
            .thenReturn(BigDecimal.valueOf(2))
        `when`(financeCalculator.calculateTotalWithVat(VatCalculationType.SUM, BigDecimal.valueOf(20), BigDecimal.valueOf(20), BigDecimal.valueOf(10)))
            .thenReturn(BigDecimal.valueOf(30))
        `when`(financeCalculator.calculateTotalWithVat(VatCalculationType.SUM, BigDecimal.valueOf(10), BigDecimal.valueOf(30), BigDecimal.valueOf(21)))
            .thenReturn(BigDecimal.valueOf(40))

        `when`(financeCalculator.calculateVat(VatCalculationType.SUM, BigDecimal.valueOf(20), BigDecimal.valueOf(5.5), BigDecimal.valueOf(10)))
            .thenReturn(BigDecimal.valueOf(10))
        `when`(financeCalculator.calculateVat(VatCalculationType.SUM, BigDecimal.valueOf(10), BigDecimal.valueOf(3), BigDecimal.valueOf(21)))
            .thenReturn(BigDecimal.valueOf(11))
        `when`(financeCalculator.calculateVat(VatCalculationType.SUM, BigDecimal.valueOf(20), BigDecimal.valueOf(20), BigDecimal.valueOf(10)))
            .thenReturn(BigDecimal.valueOf(100))
        `when`(financeCalculator.calculateVat(VatCalculationType.SUM, BigDecimal.valueOf(10), BigDecimal.valueOf(30), BigDecimal.valueOf(21)))
            .thenReturn(BigDecimal.valueOf(200))

        val order = mapper.dtoToDo(orderDto)

        assertThat(order!!.totalPrice).isEqualTo(BigDecimal(3))
        assertThat(order.totalVat).isEqualTo(BigDecimal(21))
        assertThat(order.totalDeliveredPrice).isEqualTo(BigDecimal(70))
        assertThat(order.totalDeliveredVat).isEqualTo(BigDecimal(300))
    }
}