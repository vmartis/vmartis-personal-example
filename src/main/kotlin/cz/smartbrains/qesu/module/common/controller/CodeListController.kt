package cz.smartbrains.qesu.module.common.controller

import cz.smartbrains.qesu.module.common.service.CodeListService
import cz.smartbrains.qesu.module.common.dto.CodeListDto
import cz.smartbrains.qesu.module.common.dto.validation.OnCreate
import cz.smartbrains.qesu.module.common.dto.validation.OnUpdate
import lombok.AccessLevel
import lombok.Getter
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import javax.validation.Valid
import javax.validation.groups.Default

@Getter(AccessLevel.PROTECTED)
abstract class CodeListController<T : CodeListDto>(private val service: CodeListService<*, T>) {
    @RequestMapping(path = [""], method = [RequestMethod.GET])
    fun list(): List<T> {
        return service.findAll()
    }

    @RequestMapping(path = [""], method = [RequestMethod.POST])
    fun create(@Validated(OnCreate::class, Default::class) @RequestBody codeList: T): T {
        return service.create(codeList)
    }

    @RequestMapping(path = [""], method = [RequestMethod.PUT])
    fun update(@Validated(OnUpdate::class, Default::class) @RequestBody codeList: T): T {
        return service.update(codeList)
    }

    @RequestMapping(path = ["/{id}"], method = [RequestMethod.DELETE])
    fun delete(@PathVariable id: @Valid Long) {
        service.delete(id)
    }

    @RequestMapping(path = ["/{id}/position/{position}"], method = [RequestMethod.PUT])
    fun updatePosition(@PathVariable id: Long, @PathVariable position: Int): CodeListDto {
        return service.updatePosition(id, position)
    }
}