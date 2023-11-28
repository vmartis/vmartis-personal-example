package cz.smartbrains.qesu.module.common.mapper

import cz.smartbrains.qesu.module.user.dto.UserDto
import cz.smartbrains.qesu.module.user.entity.User
import org.mapstruct.Mapper

@Mapper(componentModel = "spring")
abstract class AuditableMapper {
    fun userDoToDto(user: User?): UserDto? {
        if (user == null) {
            return null
        }
        val userDto = UserDto()
        userDto.firstname = user.firstname
        userDto.surname = user.surname
        return userDto
    }
}