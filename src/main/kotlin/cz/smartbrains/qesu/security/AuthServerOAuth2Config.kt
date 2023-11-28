package cz.smartbrains.qesu.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter

@Configuration
@Suppress("deprecation")
class AuthServerOAuth2Config {
    @Configuration
    @EnableResourceServer
    protected class ResourceServerConfiguration : ResourceServerConfigurerAdapter() {
        private val SYSTEM_CHECK = "SYSTEM_CHECK"

        @Autowired
        private val authenticationEntryPoint: Http401UnauthenticatedEntryPoint? = null

        @Autowired
        private val ajaxLogoutSuccessHandler: AjaxLogoutSuccessHandler? = null
        @Throws(Exception::class)
        override fun configure(http: HttpSecurity) {
            http.csrf().disable()
            http
                    .exceptionHandling()
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .and()
                    .logout()
                    .logoutUrl("/api/logout")
                    .logoutSuccessHandler(ajaxLogoutSuccessHandler)
                    .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .authorizeRequests()
                    .antMatchers("/api/configuration").permitAll()
                    .antMatchers("/api/**").authenticated()
                    .and()
                    .authorizeRequests().requestMatchers(EndpointRequest.toAnyEndpoint())
                    .hasAuthority(SYSTEM_CHECK)
                    .and()
                    .httpBasic()
        }
    }

    @Configuration
    @EnableAuthorizationServer
    protected class AuthorizationServerConfiguration
}