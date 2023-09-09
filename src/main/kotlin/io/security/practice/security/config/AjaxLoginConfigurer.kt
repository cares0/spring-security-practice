package io.security.practice.security.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.security.practice.security.authentication.ajax.AjaxAuthenticationProvider
import io.security.practice.security.authentication.ajax.AjaxLoginProcessingFilter
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.HttpSecurityBuilder
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.RememberMeServices
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.security.web.util.matcher.RequestMatcher
import org.springframework.stereotype.Component

class AjaxLoginConfigurer<H : HttpSecurityBuilder<H>>: AbstractAuthenticationFilterConfigurer<H, AjaxLoginConfigurer<H>, AjaxLoginProcessingFilter>(
    AjaxLoginProcessingFilter(), "/api/login"
) {

    private var objectMapper: ObjectMapper? = null
    private var authenticationProvider: AuthenticationProvider? = null
    private var authenticationSuccessHandler: AuthenticationSuccessHandler? = null
    private var authenticationFailureHandler: AuthenticationFailureHandler? = null

    override fun configure(http: H) {
        if (objectMapper == null) this.objectMapper = ObjectMapper()

        val authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder::class.java)
        authenticationManagerBuilder.authenticationProvider(authenticationProvider)

        authenticationFilter.setAuthenticationManager(authenticationManagerBuilder.build())
        authenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler)
        authenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler)

        val sessionStrategy = http.getSharedObject(SessionAuthenticationStrategy::class.java)
        authenticationFilter.setSessionAuthenticationStrategy(sessionStrategy)

        val rememberMeService = http.getSharedObject(RememberMeServices::class.java)
        authenticationFilter.rememberMeServices = rememberMeService

        http.setSharedObject(AjaxLoginProcessingFilter::class.java, authenticationFilter)
        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
    }

    override fun createLoginProcessingUrlMatcher(loginProcessingUrl: String): RequestMatcher {
        return AntPathRequestMatcher(loginProcessingUrl)
    }

    fun ajaxSuccessHandler(handler: AuthenticationSuccessHandler): AjaxLoginConfigurer<H> {
        this.authenticationSuccessHandler = handler
        return this
    }

    fun ajaxFailureHandler(handler: AuthenticationFailureHandler): AjaxLoginConfigurer<H> {
        this.authenticationFailureHandler = handler
        return this
    }

    fun ajaxAuthenticationProvider(provider: AuthenticationProvider): AjaxLoginConfigurer<H> {
        this.authenticationProvider = provider
        return this
    }

    fun objectMapper(objectMapper: ObjectMapper): AjaxLoginConfigurer<H> {
        this.authenticationFilter.objectMapper = objectMapper
        return this
    }

}