package cz.smartbrains.qesu.module.stock.movement.mapper

import cz.smartbrains.qesu.module.common.mapper.AuditableMapper
import cz.smartbrains.qesu.module.company.mapper.CompanyMapper
import cz.smartbrains.qesu.module.item.mapper.ItemMapper
import cz.smartbrains.qesu.module.stock.mapper.StockMapper
import cz.smartbrains.qesu.module.stock.movement.dto.OutcomeStockMovementDto
import cz.smartbrains.qesu.module.stock.movement.dto.StockMovementItemDto
import cz.smartbrains.qesu.module.stock.movement.type.OutcomeStockMovementType
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
class OutcomeStockMovementMapperImplTest {
    @Mock
    private val auditableMapper: AuditableMapper? = null

    @Mock
    private val companyMapper: CompanyMapper? = null

    @Mock
    private val stockMovementItemMapper: StockMovementItemMapper? = null

    @Mock
    private val stockMapper: StockMapper? = null

    @Mock
    private val itemMapper: ItemMapper? = null

    @InjectMocks
    private val mapper: OutcomeStockMovementMapperImpl = OutcomeStockMovementMapperImpl()

    @Test
    fun dtoToDo_totalPrices_areCorrectAndPositive() {
        val dto = OutcomeStockMovementDto()
        dto.id = 1L
        dto.type = OutcomeStockMovementType.PRODUCTION
        dto.items = Lists.newArrayList(
                StockMovementItemDto(null, BigDecimal.valueOf(50L), BigDecimal.valueOf(20L), null, arrayListOf()),
                StockMovementItemDto(null, BigDecimal.valueOf(5L), BigDecimal.valueOf(10L), null, arrayListOf())
        )
        val entity = mapper.dtoToDo(dto)
        Assertions.assertThat(entity!!.id).isEqualTo(dto.id)
        Assertions.assertThat(entity.type).isEqualTo(OutcomeStockMovementType.PRODUCTION)
        Assertions.assertThat(entity.totalPrice).isEqualTo(BigDecimal.valueOf(-1050L).setScale(2, RoundingMode.HALF_UP))
        Assertions.assertThat(entity.items).hasSize(2)
        Assertions.assertThat(entity.items[0].totalPrice).isEqualTo(BigDecimal.valueOf(-1000L).setScale(2, RoundingMode.HALF_UP))
        Assertions.assertThat(entity.items[0].amount).isEqualTo(BigDecimal.valueOf(-50L))
        Assertions.assertThat(entity.items[1].totalPrice).isEqualTo(BigDecimal.valueOf(-50L).setScale(2, RoundingMode.HALF_UP))
        Assertions.assertThat(entity.items[1].amount).isEqualTo(BigDecimal.valueOf(-5L))
    }
}