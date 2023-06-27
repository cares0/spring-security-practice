package io.security.practice.request

import io.security.practice.domain.Account
import org.springframework.security.crypto.password.PasswordEncoder

class AccountRequest(
    val username: String,
    val password: String,
    val email: String,
    val age: String,
    val role: String,
) {

    fun toDomainObject(
        passwordEncoder: PasswordEncoder,
    ): Account {
        return Account(
            username = username,
            password = passwordEncoder.encode(password),
            email = email,
            age = age,
            role = role,
        )
    }
}