package io.security.practice.security.authentication.ajax

import com.fasterxml.jackson.databind.ObjectMapper
import io.security.practice.domain.Account
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.AuthenticationSuccessHandler

class AjaxSuccessHandler(
    private val objectMapper: ObjectMapper,

) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val account = authentication.principal as Account

        response.status = HttpStatus.OK.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE

        objectMapper.writeValue(response.writer, account)
    }

}