package cz.smartbrains.qesu.module.stock.movement.mapper

import cz.smartbrains.qesu.module.common.mapper.AuditableMapper
import cz.smartbrains.qesu.module.company.mapper.CompanyMapper
import cz.smartbrains.qesu.module.item.mapper.ItemMapper
import cz.smartbrains.qesu.module.stock.mapper.StockMapper
import cz.smartbrains.qesu.module.stock.movement.dto.IncomeStockMovementDto
import cz.smartbrains.qesu.module.stock.movement.dto.StockMovementItemDto
import cz.smartbrains.qesu.module.stock.movement.repository.OutcomeStockMovementRepository
import cz.smartbrains.qesu.module.stock.movement.type.IncomeStockMovementType
import org.assertj.core.api.Assertions
import org.assertj.core.util.Lists
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.math.RoundingMode

@ExtendWith(MockitoExtension::class)
class IncomeStockMovementMapperImplTest {
    @Mock
    private lateinit var auditableMapper: AuditableMapper

    @Mock
    private lateinit var companyMapper: CompanyMapper

    @Mock
    private lateinit var stockMovementItemMapper: StockMovementItemMapper

    @Mock
    private lateinit var outcomeStockMovementRepository: OutcomeStockMovementRepository

    @Mock
    private lateinit var stockMapper: StockMapper

    @Mock
    private lateinit var itemMapper: ItemMapper

    @InjectMocks
    private val mapper: IncomeStockMovementMapperImpl = IncomeStockMovementMapperImpl()

    @Test
    fun dtoToDo_totalPrices_areCorrectAndPositive() {
        val dto = IncomeStockMovementDto()
        dto.id = 1L
        dto.type = IncomeStockMovementType.PRODUCTION
        dto.deliveryNote = "abcd"
        dto.items = Lists.newArrayList(
                StockMovementItemDto(null, BigDecimal.valueOf(50L), BigDecimal.valueOf(20L), null, arrayListOf()),
                StockMovementItemDto(null, BigDecimal.valueOf(5L), BigDecimal.valueOf(10L), null, arrayListOf())
        )
        val entity = mapper.dtoToDo(dto)
        Assertions.assertThat(entity!!.id).isEqualTo(dto.id)
        Assertions.assertThat(entity.type).isEqualTo(IncomeStockMovementType.PRODUCTION)
        Assertions.assertThat(entity.deliveryNote).isEqualTo("abcd")
        Assertions.assertThat(entity.totalPrice).isEqualTo(BigDecimal.valueOf(1050L).setScale(2, RoundingMode.HALF_UP))
        Assertions.assertThat(entity.items).hasSize(2)
        Assertions.assertThat(entity.items[0].totalPrice).isEqualTo(BigDecimal.valueOf(1000L).setScale(2, RoundingMode.HALF_UP))
        Assertions.assertThat(entity.items[1].totalPrice).isEqualTo(BigDecimal.valueOf(50L).setScale(2, RoundingMode.HALF_UP))
    }
}