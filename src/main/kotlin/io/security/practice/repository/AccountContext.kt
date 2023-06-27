package io.security.practice.repository

import io.security.practice.domain.Account
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User

class AccountContext(
    val account: Account,
    authorities: List<GrantedAuthority>,
) : User(
    account.username,
    account.password,
    authorities,
) {
}