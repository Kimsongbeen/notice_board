package org.example.securityapp.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "ok";
    }

    @GetMapping("/auth/test")
    public String authTest() {
        return "auth ok";
    }
}
