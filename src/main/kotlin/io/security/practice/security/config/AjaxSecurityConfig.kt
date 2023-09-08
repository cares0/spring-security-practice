package io.security.practice.security.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.security.practice.security.authentication.ajax.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher


@Order(0)
@Configuration
class AjaxSecurityConfig(
    private val objectMapper: ObjectMapper,
    private val userDetailsService: UserDetailsService,
    private val passwordEncoder: PasswordEncoder,
) {

    @Bean
    fun ajaxFilterChain(http: HttpSecurity, builder: AuthenticationManagerBuilder) : SecurityFilterChain {
        http.authorizeHttpRequests { authorize ->
            authorize
                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/messages")).hasRole("MANAGER")
                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/**")).permitAll()
                .anyRequest().permitAll()
        }

        http.exceptionHandling { exception ->
            exception.authenticationEntryPoint(AjaxAuthenticationEntryPoint())
            exception.accessDeniedHandler(AjaxAccessDeniedHandler())
        }

        http.addFilterBefore(ajaxLoginProcessingFilter(http), UsernamePasswordAuthenticationFilter::class.java)
        http.csrf { csrf -> csrf.disable() }

        return http.build()
    }

    @Bean
    fun ajaxAuthenticationFailureHandler(): AuthenticationFailureHandler {
        return AjaxFailureHandler(objectMapper)
    }

    @Bean
    fun ajaxAuthenticationSuccessHandler(): AuthenticationSuccessHandler {
        return AjaxSuccessHandler(objectMapper)
    }

    @Bean
    fun ajaxAuthenticationProvider(): AuthenticationProvider {
        return AjaxAuthenticationProvider(userDetailsService, passwordEncoder)
    }

    @Bean
    fun ajaxLoginProcessingFilter(http: HttpSecurity): AjaxLoginProcessingFilter {
        val ajaxLoginProcessingFilter = AjaxLoginProcessingFilter(objectMapper)

        val authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder::class.java)
        authenticationManagerBuilder.authenticationProvider(ajaxAuthenticationProvider())

        ajaxLoginProcessingFilter.setAuthenticationManager(authenticationManagerBuilder.build())
        ajaxLoginProcessingFilter.setAuthenticationSuccessHandler(ajaxAuthenticationSuccessHandler())
        ajaxLoginProcessingFilter.setAuthenticationFailureHandler(ajaxAuthenticationFailureHandler())
        return ajaxLoginProcessingFilter
    }

}