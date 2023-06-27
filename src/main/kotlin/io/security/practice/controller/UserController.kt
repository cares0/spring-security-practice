package io.security.practice.controller

import io.security.practice.request.AccountRequest
import io.security.practice.service.UserService
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping

@Controller
class UserController(
    private val userService: UserService,
) {

    @GetMapping("/mypage")
    fun myPage(): String {
        return "user/mypage"
    }

    @GetMapping("/users")
    fun createUser(): String {
        return "user/login/register"
    }

    @PostMapping("/users")
    fun createUser(accountRequest: AccountRequest): String {

        userService.createUser(accountRequest)

        return "redirect:/"
    }

}