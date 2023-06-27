package io.security.practice.security.authentication

import io.security.practice.repository.AccountContext
import io.security.practice.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

@Component
class CustomUserDetailsService(
    private val userRepository: UserRepository,
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val account = userRepository.findByUsername(username) ?: throw UsernameNotFoundException(username)

        return AccountContext(account, listOf(SimpleGrantedAuthority(account.role)))
    }

}