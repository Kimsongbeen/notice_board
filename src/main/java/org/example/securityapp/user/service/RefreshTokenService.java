package org.example.securityapp.user.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RefreshTokenService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final long REFRESH_TOKEN_EXPIRE = 1000 * 60 * 60 * 24 * 7; // 7DAY

    public RefreshTokenService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Refresh Token 저장
    public void saveRefreshToken(String userId, String refreshToken) {
        redisTemplate.opsForValue()
                .set("refresh:" + userId, refreshToken, REFRESH_TOKEN_EXPIRE, TimeUnit.MILLISECONDS);
    }

    // Refresh Token 검증
    public boolean validRefreshToken(String userId, String refreshToken) {
        String storedToken = (String) redisTemplate.opsForValue().get("refresh:" + userId);
        return refreshToken.equals(storedToken);
    }

    // Refresh Token 삭제
    public void deleteRefreshToken(String userId) {
        redisTemplate.delete("refresh:" + userId);
    }
}
