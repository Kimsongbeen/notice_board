package org.example.securityapp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    @GetMapping("/secure")
    public ResponseEntity<String> secure() {
        return ResponseEntity.ok("SECURE");
    }
}
