package io.security.practice.security.authentication.ajax

import io.security.practice.repository.AccountContext
import io.security.practice.security.authentication.form.FormWebAuthenticationDetails
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder

class AjaxAuthenticationProvider(
    private val userDetailsService: UserDetailsService,
    private val passwordEncoder: PasswordEncoder,
) : AuthenticationProvider {

    override fun authenticate(authentication: Authentication): Authentication {
        val username = authentication.name
        val password = authentication.credentials as String

        val accountContext = userDetailsService.loadUserByUsername(username) as AccountContext

        if (!passwordEncoder.matches(password, accountContext.account.password)) {
            throw BadCredentialsException("FAIL")
        }

        return AjaxAuthenticationToken.authenticated(
            accountContext.account, accountContext.authorities
        )
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return AjaxAuthenticationToken::class.java.isAssignableFrom(authentication)
    }
}