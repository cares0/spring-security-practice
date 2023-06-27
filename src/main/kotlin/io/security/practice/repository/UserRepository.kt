package io.security.practice.repository

import io.security.practice.domain.Account
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<Account, Long> {

    fun findByUsername(username: String): Account?

}