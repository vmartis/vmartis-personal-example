package cz.smartbrains.qesu.module.takings.dto

import java.time.LocalDate

class TakingsFilter(var companyBranchId: Long? = null,
                    var companyBranchIds: List<Long>? = null,
                    var date: LocalDate? = null,
                    var dateFrom: LocalDate? = null,
                    var dateTo: LocalDate? = null,
                    var currency: String? = null)