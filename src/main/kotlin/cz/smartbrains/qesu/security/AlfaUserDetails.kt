package cz.smartbrains.qesu.security

import lombok.ToString
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

@ToString(exclude = ["password"])
class AlfaUserDetails(val id: Long,
                      private val username: String,
                      private val password: String,
                      private val active: Boolean,
                      private val authorities: Collection<GrantedAuthority?>) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority?> {
        return authorities
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return active
    }

    companion object {
        private const val serialVersionUID = -5423806380249219024L
    }
}