package io.security.practice.controller

import io.security.practice.request.AccountRequest
import io.security.practice.service.UserService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import java.lang.Exception

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

    @GetMapping("/login")
    fun login(
        @RequestParam(required = false) error: String?,
        @RequestParam(required = false) exception: String?,
        model: Model
    ): String {

        model.addAttribute("error", error)
        model.addAttribute("exception", exception)

        return "user/login/login"
    }

    @GetMapping("/logout")
    fun logout(
        request: HttpServletRequest,
        response: HttpServletResponse,
    ): String {
        val authentication = SecurityContextHolder.getContext().authentication

        if (authentication != null) {
            SecurityContextLogoutHandler().logout(request, response, authentication)
        }

        return "redirect:/login"
    }

}