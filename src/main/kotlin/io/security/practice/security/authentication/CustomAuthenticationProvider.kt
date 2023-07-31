package io.security.practice.security.authentication

import io.security.practice.repository.AccountContext
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder

class CustomAuthenticationProvider(
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


        val details = authentication.details as FormWebAuthenticationDetails

        if (details.secretKey == null || details.secretKey != "secret") {
            throw InsufficientAuthenticationException("InsufficientAuthenticationException")
        }

        return UsernamePasswordAuthenticationToken.authenticated(
            accountContext.account, null, accountContext.authorities
        )
    }

    override fun supports(authentication: Class<*>?): Boolean {
        return UsernamePasswordAuthenticationToken::class.java.isAssignableFrom(authentication)
    }

}