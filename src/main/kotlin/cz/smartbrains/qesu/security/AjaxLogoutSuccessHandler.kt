package cz.smartbrains.qesu.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Spring Security logout handler, specialized for Ajax requests.
 */
@Component
@Suppress("deprecation")
class AjaxLogoutSuccessHandler : AbstractAuthenticationTargetUrlRequestHandler(), LogoutSuccessHandler {
    @Autowired
    private val consumerTokenServices: ConsumerTokenServices? = null
    override fun onLogoutSuccess(request: HttpServletRequest, response: HttpServletResponse,
                                 authentication: Authentication?) {
        // Request the token
        val token = request.getHeader("authorization")
        if (token != null && token.startsWith(BEARER_AUTHENTICATION)) {
            consumerTokenServices!!.revokeToken(token.replaceFirst(BEARER_AUTHENTICATION.toRegex(), ""))
        }
        response.status = HttpServletResponse.SC_OK
    }

    companion object {
        const val BEARER_AUTHENTICATION = "Bearer "
    }
}