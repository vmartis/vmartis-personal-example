package cz.smartbrains.qesu.security

import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Returns a 401 error code (Unauthorized) to the client.
 */
@Component
class Http401UnauthenticatedEntryPoint : AuthenticationEntryPoint {
    private val log = LoggerFactory.getLogger(Http401UnauthenticatedEntryPoint::class.java)

    /**
     * Always returns a 401 error code to the client.
     */
    @Throws(IOException::class, ServletException::class)
    override fun commence(request: HttpServletRequest, response: HttpServletResponse, arg2: AuthenticationException) {
        log.debug("Pre-authenticated entry point called. Rejecting access")
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Denied")
    }
}