package org.example.securityapp.user.controller;

import org.example.securityapp.auth.JwtProvider;
import org.example.securityapp.user.service.RefreshTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class LogoutController {

    private final RefreshTokenService refreshTokenService;
    private final JwtProvider jwtProvider;


    public LogoutController(RefreshTokenService refreshTokenService, JwtProvider jwtProvider) {
        this.refreshTokenService = refreshTokenService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String refreshTokenHeader) {
        if (refreshTokenHeader == null || !refreshTokenHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Refresh Token missing");
        }

        String refreshToken = refreshTokenHeader.substring(7);
        String userId = jwtProvider.getSubject(refreshToken);

        refreshTokenService.deleteRefreshToken(userId);

        return ResponseEntity.ok("Logged out Successfully");
    }
}