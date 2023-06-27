package io.security.practice.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.GenerationType.*
import jakarta.persistence.Id

@Entity
class Account(

    val username: String,
    val password: String,
    val email: String,
    val age: String,
    val role: String,

) {

    @Id @GeneratedValue(strategy = IDENTITY)
    val id: Long = 0

}