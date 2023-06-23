package io.security.practice.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
class SecurityConfig {

    @Bean
    fun authenticationManager(auth: AuthenticationManagerBuilder): InMemoryUserDetailsManager {
        val password = passwordEncoder().encode("1111")

        val userDetails = listOf(
            User.withUsername("user").password(password).roles("USER").build(),
            User.withUsername("manager").password(password).roles("MANAGER").build(),
            User.withUsername("admin").password(password).roles("ADMIN").build(),
        )

        return InMemoryUserDetailsManager(userDetails)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.formLogin(Customizer.withDefaults())

        http
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/")).permitAll()
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/mypage")).hasRole("USER")
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/messages")).hasRole("MANAGER")
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/config")).hasRole("ADMIN")
                    .anyRequest().authenticated()
            }

        http
            .headers { headers ->
                headers.frameOptions { frameOption -> frameOption.disable() }
            }

        http
            .csrf { csrf ->
                csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**"))
            }

        return http.build()
    }

}