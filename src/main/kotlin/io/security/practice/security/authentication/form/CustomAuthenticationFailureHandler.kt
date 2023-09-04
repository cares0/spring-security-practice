package io.security.practice.security.authentication.form

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationFailureHandler : SimpleUrlAuthenticationFailureHandler() {

    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException,
    ) {
        var errorMessage = "Invalid Username or Password"

        if (exception is BadCredentialsException) {
            errorMessage = "Invalid Username or Password"
        } else if (exception is InsufficientAuthenticationException) {
            errorMessage = "Invalid Secret Key"
        }

        setDefaultFailureUrl("/login?error=true&exception=$errorMessage")

        super.onAuthenticationFailure(request, response, exception)
    }
}