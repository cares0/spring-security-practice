package io.security.practice.security.authorization

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

class CustomAccessDeniedHandler(
    private val errorPage: String
) : AccessDeniedHandler {



    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        var deniedUrl = errorPage + "?exception=" + accessDeniedException.message
        response.sendRedirect(deniedUrl)
    }
}