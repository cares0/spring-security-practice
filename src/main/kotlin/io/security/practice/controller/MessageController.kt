package io.security.practice.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class MessageController {

    @GetMapping("messages")
    fun message(): String {
        return "/user/messages"
    }
}