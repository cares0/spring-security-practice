package io.security.practice.service

import io.security.practice.repository.UserRepository
import io.security.practice.request.AccountRequest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    fun createUser(accountRequest: AccountRequest) {
        userRepository.save(
            accountRequest.toDomainObject(passwordEncoder)
        )
    }

}