package org.example.securityapp.user.controller;

import org.apache.coyote.Response;
import org.example.securityapp.auth.JwtProvider;
import org.example.securityapp.user.service.RefreshTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class ReissueController {

    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    public ReissueController(JwtProvider jwtProvider, RefreshTokenService refreshTokenService) {
        this.jwtProvider = jwtProvider;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(@RequestHeader("Authorization") String refreshTokenHeader) {
        if(refreshTokenHeader == null || !refreshTokenHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh Token missing");
        }

        String refreshToken = refreshTokenHeader.substring(7);
        String userId = jwtProvider.getSubject(refreshToken);

        if(!refreshTokenService.validRefreshToken(userId, refreshToken)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Refresh Token");
        }

        // 새로운 Access Token 발급
        UserDetails userDetails = jwtProvider.getUserDetails(refreshToken);
        String newAccessToken = jwtProvider.createToken(userDetails);

        // Refresh Token 회전 (선택)
        String newRefreshToken = jwtProvider.createRefreshToken(userDetails);
        refreshTokenService.saveRefreshToken(userId, newRefreshToken);

        return ResponseEntity.ok(Map.of(
                "accessToken", newAccessToken,
                "refreshToken", newRefreshToken
        ));
    }
}
