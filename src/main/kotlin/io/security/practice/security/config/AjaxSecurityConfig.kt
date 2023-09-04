package io.security.practice.security.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.security.practice.security.authentication.ajax.AjaxAuthenticationProvider
import io.security.practice.security.authentication.ajax.AjaxLoginProcessingFilter
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
                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/**")).authenticated()
                .anyRequest().authenticated()
        }

        http.addFilterBefore(ajaxLoginProcessingFilter(http), UsernamePasswordAuthenticationFilter::class.java)
        http.csrf { csrf -> csrf.disable() }

        return http.build()
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
        return ajaxLoginProcessingFilter
    }

}