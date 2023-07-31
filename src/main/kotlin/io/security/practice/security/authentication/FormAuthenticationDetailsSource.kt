package io.security.practice.security.authentication

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.authentication.AuthenticationDetailsSource
import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.stereotype.Component

@Component
class FormAuthenticationDetailsSource : AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

    override fun buildDetails(context: HttpServletRequest): WebAuthenticationDetails {
        return FormWebAuthenticationDetails(context)
    }

}