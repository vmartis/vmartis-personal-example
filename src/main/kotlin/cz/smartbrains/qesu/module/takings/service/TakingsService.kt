package cz.smartbrains.qesu.module.takings.service

import cz.smartbrains.qesu.module.takings.dto.TakingsDto
import cz.smartbrains.qesu.module.takings.dto.TakingsFilter
import cz.smartbrains.qesu.module.takings.dto.TakingsTransferDto
import cz.smartbrains.qesu.security.AlfaUserDetails
import org.springframework.security.access.prepost.PreAuthorize

interface TakingsService {
    @PreAuthorize("hasAnyAuthority('TAKINGS', 'TAKINGS_TRANSFER', 'TAKINGS_OVERVIEW')")
    fun findBy(filter: TakingsFilter): List<TakingsDto>

    @PreAuthorize("hasAnyAuthority('TAKINGS')")
    fun create(takingsDto: TakingsDto, user: AlfaUserDetails): TakingsDto

    @PreAuthorize("hasAnyAuthority('TAKINGS')")
    fun update(takingsDto: TakingsDto, user: AlfaUserDetails): TakingsDto

    @PreAuthorize("hasAnyAuthority('TAKINGS_TRANSFER')")
    fun transfer(takingsTransferDto: TakingsTransferDto, user: AlfaUserDetails)

    @PreAuthorize("hasAnyAuthority('TAKINGS')")
    fun delete(id: Long)
}