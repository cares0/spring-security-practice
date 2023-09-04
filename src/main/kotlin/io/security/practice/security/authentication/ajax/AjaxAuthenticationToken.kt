package io.security.practice.security.authentication.ajax

import io.security.practice.domain.Account
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class AjaxAuthenticationToken(
    val ajaxPrincipal: Any,
    val ajaxCredentials: Any,
    authorities: Collection<GrantedAuthority> = mutableListOf()
) : AbstractAuthenticationToken(
    authorities
) {

    override fun getCredentials(): Any {
        return ajaxCredentials
    }

    override fun getPrincipal(): Any {
        return ajaxPrincipal
    }

    companion object {

        fun unauthenticated(
            username: Any,
            credentials: Any,
        ): AjaxAuthenticationToken {
            return AjaxAuthenticationToken(username, credentials)
        }

        fun authenticated(
            account: Account,
            authorities: Collection<GrantedAuthority>,
        ): AjaxAuthenticationToken {
            return AjaxAuthenticationToken(account, account.password, authorities)
        }

    }
}