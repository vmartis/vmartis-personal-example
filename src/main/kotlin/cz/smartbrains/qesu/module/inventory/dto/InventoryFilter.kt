package cz.smartbrains.qesu.module.inventory.dto

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import java.time.LocalDate

@Data
@Builder
@AllArgsConstructor
class InventoryFilter(val stockId: Long? = null,
                      val dateFrom: LocalDate? = null,
                      val dateTo: LocalDate? = null,
                      val itemId: Long? = null,
                      val itemBatchNumber: String? = null) {
}