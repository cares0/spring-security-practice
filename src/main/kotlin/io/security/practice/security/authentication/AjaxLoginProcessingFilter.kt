package io.security.practice.security.authentication

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

class AjaxLoginProcessingFilter(
    private val objectMapper: ObjectMapper,
) : AbstractAuthenticationProcessingFilter(
    AntPathRequestMatcher("/api/login")
) {

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        if (!isAjax(request)) throw IllegalStateException("Ajax request no")

        val loginDto = objectMapper.readValue(request.reader, AjaxAccountLoginDto::class.java)

        if (loginDto.username.isEmpty() || loginDto.password.isEmpty()) {
            throw IllegalStateException("Username or Password is Empty")
        }

        val token = AjaxAuthenticationToken.unauthenticated(loginDto.username, loginDto.password)

        return authenticationManager.authenticate(token)
    }

    private fun isAjax(request: HttpServletRequest): Boolean {
        return "XMLHttpRequest" == request.getHeader("X-Requested-With")
    }
}