package cz.smartbrains.qesu.module.common.service

import cz.smartbrains.qesu.module.user.type.PermissionType
import cz.smartbrains.qesu.module.common.dto.CodeListDto
import cz.smartbrains.qesu.module.common.entity.CodeList
import cz.smartbrains.qesu.module.common.mapper.CodeListMapper
import cz.smartbrains.qesu.module.common.repository.CodeListRepository
import cz.smartbrains.qesu.security.PermissionChecker
import lombok.AccessLevel
import lombok.Getter
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.util.stream.Collectors

@Getter(AccessLevel.PROTECTED)
open class CodeListServiceImpl<E : CodeList, T : CodeListDto>(private val permissionChecker: PermissionChecker,
                                                           private val orderableEntityService: OrderableEntityService,
                                                           private val repository: CodeListRepository<E>,
                                                           private val mapper: CodeListMapper<E, T>,
                                                           private val writeRequiredPermission: PermissionType?) : CodeListService<E, T> {
    @Transactional(readOnly = true)
    override fun findAll(): List<T> {
        val byOrderByLabelAsc = repository.findByOrderByOrderAsc()
        return byOrderByLabelAsc.stream().map { entity: E -> mapper.doToDto(entity) }.collect(Collectors.toList())
    }

    @Transactional
    override fun create(codeListDto: T): T {
        checkWriteRole()
        codeListDto.validFrom = LocalDate.now()
        codeListDto.order = orderableEntityService.newItemOrder(repository)
        val entity = mapper.dtoToDo(codeListDto)
        repository.save(entity)
        codeListDto.id = entity.id
        return codeListDto
    }

    @Transactional
    override fun update(codeListDto: T): T {
        checkWriteRole()
        val updatedEntity = mapper.dtoToDo(codeListDto)
        repository.save(updatedEntity)
        return codeListDto
    }

    @Transactional
    override fun delete(id: Long) {
        checkWriteRole()
        repository.deleteById(id)
    }

    @Transactional
    override fun updatePosition(id: Long, position: Int): T {
        checkWriteRole()
        val entity = orderableEntityService.updatePosition(repository, id, position)
        return mapper.doToDto(entity)
    }

    private fun checkWriteRole() {
        if (writeRequiredPermission != null) {
            permissionChecker.checkPermission(writeRequiredPermission)
        }
    }
}