package io.security.practice.security.authentication.ajax

import com.fasterxml.jackson.databind.ObjectMapper
import io.security.practice.domain.Account
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler

class AjaxFailureHandler(
    private val objectMapper: ObjectMapper,
) : AuthenticationFailureHandler {

    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException
    ) {
        var errorMessage = "Invalid Username or Password"

        if (exception is BadCredentialsException) {
            errorMessage = "Invalid Username or Password"
        } else if (exception is InsufficientAuthenticationException) {
            errorMessage = "Invalid Secret Key"
        }

        response.status = HttpStatus.UNAUTHORIZED.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        objectMapper.writeValue(response.writer, errorMessage)
    }

}