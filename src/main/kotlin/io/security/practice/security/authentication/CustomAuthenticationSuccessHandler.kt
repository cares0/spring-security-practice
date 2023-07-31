package io.security.practice.security.authentication

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.DefaultRedirectStrategy
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.security.web.savedrequest.HttpSessionRequestCache
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationSuccessHandler : SimpleUrlAuthenticationSuccessHandler() {

    private val requestCache = HttpSessionRequestCache()

    private val redirectStrategy = DefaultRedirectStrategy()

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {

        defaultTargetUrl = "/"

        val savedRequest = requestCache.getRequest(request, response)

        if (savedRequest != null) {
            val targetUrl = savedRequest.redirectUrl

            redirectStrategy.sendRedirect(request, response, targetUrl)
        } else {
            redirectStrategy.sendRedirect(request, response, defaultTargetUrl)
        }

        super.onAuthenticationSuccess(request, response, authentication)
    }
}