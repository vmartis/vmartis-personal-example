package cz.smartbrains.qesu.security

import cz.smartbrains.qesu.module.user.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.stereotype.Component

/**
 * Handles success loging and update lastLoggedIn timestamp in user entity.
 */
@Component
class LastLoggedInSuccessHandler(private val userService: UserService) : ApplicationListener<AuthenticationSuccessEvent> {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun onApplicationEvent(event: AuthenticationSuccessEvent) {
        if (event.source is UsernamePasswordAuthenticationToken && (event.source as UsernamePasswordAuthenticationToken).principal is AlfaUserDetails) {
            val user = event.authentication.principal as AlfaUserDetails
            log.info("Success logging of user {}", user.username)
            userService.updateLastLoggedIn(user)
        }
    }
}