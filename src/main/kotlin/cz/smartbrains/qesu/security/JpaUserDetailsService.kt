package cz.smartbrains.qesu.security

import cz.smartbrains.qesu.module.user.entity.User
import cz.smartbrains.qesu.module.user.repository.UserRepository
import cz.smartbrains.qesu.module.user.type.PermissionType
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Service
class JpaUserDetailsService(private val userRepository: UserRepository) : UserDetailsService {
    @Transactional
    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        return userRepository.findByEmail(username)
                .map { user: User ->
                    AlfaUserDetails(user.id!!,
                            user.email!!,
                            user.password!!,
                            user.active,
                            user.permissions.stream().map { roleType: PermissionType -> SimpleGrantedAuthority(roleType.name) }.collect(Collectors.toList()))
                }
                .orElseThrow { UsernameNotFoundException("User with username $username, doesn't exist.") }
    }
}