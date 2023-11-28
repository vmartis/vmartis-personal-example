package cz.smartbrains.qesu.security

import cz.smartbrains.qesu.module.user.type.PermissionType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.util.function.Predicate

/**
 * Check required permissions manually - where us not possible to use annotation @PreAuthorize
 */
@Service
class PermissionChecker {
    /**
     * Check if current user has required permission or not.
     * @param permission required permission.
     * @throws AccessDeniedException if user hasn't required permission.
     */
    @Throws(AccessDeniedException::class)
    fun checkPermission(permission: PermissionType) {
        if (!hasPermission(permission)) {
            accessDeny()
        }
    }

    /**
     * Find if current user has required permission or not.
     * @param permission required permission.
     * @return true if user has permission or else false if not.
     */
    fun hasPermission(permission: PermissionType): Boolean {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication.authorities
                .stream()
                .map { obj: GrantedAuthority -> obj.authority }
                .anyMatch(Predicate.isEqual(permission.name))
    }

    /**
     * Throws access deny exception.
     * @throws AccessDeniedException always thrown.
     */
    @Throws(AccessDeniedException::class)
    fun accessDeny() {
        throw AccessDeniedException("not.allowed")
    }
}