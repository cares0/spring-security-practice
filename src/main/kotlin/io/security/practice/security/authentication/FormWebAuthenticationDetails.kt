package io.security.practice.security.authentication

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.web.authentication.WebAuthenticationDetails

class FormWebAuthenticationDetails(
    request: HttpServletRequest,
) : WebAuthenticationDetails(
    request
) {

    val secretKey: String?

    init {
        secretKey = request.getParameter("secret_key")
    }

}