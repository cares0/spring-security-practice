package io.security.practice.security

import io.security.practice.security.authentication.CustomAuthenticationProvider
import jakarta.servlet.http.HttpServletRequest
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationDetailsSource
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler
import org.springframework.security.web.authentication.WebAuthenticationDetails
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
class SecurityConfig(
    private val userDetailsService: UserDetailsService,
    private val authenticationDetailsSource: AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails>,
    private val successHandler: AuthenticationSuccessHandler,
) {

//    @Bean
//    fun authenticationManager(auth: AuthenticationManagerBuilder): InMemoryUserDetailsManager {
//        val password = passwordEncoder().encode("1111")
//
//        val userDetails = listOf(
//            User.withUsername("user").password(password).roles("USER").build(),
//            User.withUsername("manager").password(password).roles("MANAGER", "USER").build(),
//            User.withUsername("admin").password(password).roles("ADMIN", "MANAGER", "USER").build(),
//        )
//
//        return InMemoryUserDetailsManager(userDetails)
//    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Bean
    fun authenticationProvider(): AuthenticationProvider {
        return CustomAuthenticationProvider(userDetailsService, passwordEncoder())
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.formLogin(Customizer.withDefaults())

        http
            .authorizeHttpRequests { authorize ->
                authorize
                    .requestMatchers("/", "users").permitAll()
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/mypage")).hasRole("USER")
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/messages")).hasRole("MANAGER")
                    .requestMatchers(AntPathRequestMatcher.antMatcher("/config")).hasRole("ADMIN")
                    .anyRequest().authenticated()
            }

        http
            .formLogin { formLogin ->
                formLogin
                    .loginPage("/login")
                    .loginProcessingUrl("/login_proc")
                    .defaultSuccessUrl("/")
                    .authenticationDetailsSource(authenticationDetailsSource)
                    .successHandler(successHandler)
                    .permitAll()
            }

        http.getSharedObject(AuthenticationManagerBuilder::class.java)
            .authenticationProvider(authenticationProvider())

        return http.build()
    }

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web ->
            web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                .requestMatchers(PathRequest.toH2Console())
        }
    }

}