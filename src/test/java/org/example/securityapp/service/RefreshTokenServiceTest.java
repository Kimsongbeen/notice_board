package org.example.securityapp.service;

import org.example.securityapp.user.service.RefreshTokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceTest {

    @Mock
    RedisTemplate<String, String> redisTemplate;

    @Mock
    ValueOperations<String, String> valueOperations;

    @InjectMocks
    RefreshTokenService refreshTokenService;

    @Test
    @DisplayName("Refresh Token 저장")
    void save_refresh_token(){

        given(redisTemplate.opsForValue()).willReturn(valueOperations);

        refreshTokenService.saveRefreshToken("user1", "refresh-token");

        verify(valueOperations).set(
                eq("refresh:user1"),
                eq("refresh-token"),
                Mockito.anyLong(),
                eq(TimeUnit.MILLISECONDS)
        );
    }

    @Test
    @DisplayName("Refresh Token 검증 성공")
    void valid_refresh_token(){

        given(redisTemplate.opsForValue()).willReturn(valueOperations);

        given(valueOperations.get("refresh:user1"))
                .willReturn("refresh-token");

        boolean result = refreshTokenService.validRefreshToken("user1", "refresh-token");

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Refresh Token 검증 실패")
    void invalid_refresh_token(){

        given(redisTemplate.opsForValue()).willReturn(valueOperations);

        given(valueOperations.get("refresh:user1"))
                .willReturn("another-token");

        boolean result = refreshTokenService.validRefreshToken("user1", "refresh-token");

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("로그아웃 시 Refresh Token 삭제")
    void delete_refresh_token(){
        refreshTokenService.deleteRefreshToken("user1");

        verify(redisTemplate).delete("refresh:user1");
    }

}
