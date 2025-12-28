package org.example.securityapp;

import org.example.securityapp.auth.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
class JwtProviderTest {

    private final JwtProvider jwtProvider = new JwtProvider();

    @Test
    @DisplayName("UserDetails로 JWT 토큰을 정상적으로 발행한다")
    void createToken_success() {
        // given
        UserDetails userDetails = new User(
                "user",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // when
        String token = jwtProvider.createToken(userDetails);

        // then
        assertThat(token).isNotNull();
        assertThat(token.split("\\.")).hasSize(3); // JWT 형식 검증
    }

    @Test
    @DisplayName("발행된 토큰은 validateToken 검증을 통과한다")
    void validateToken_success() {
        // given
        UserDetails userDetails = new User(
                "user",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        String token = jwtProvider.createToken(userDetails);

        // when
        boolean result = jwtProvider.validateToken(token);

        // then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("JWT 토큰에서 Authentication을 정상적으로 복원한다")
    void getAuthentication_success() {
        // given
        UserDetails userDetails = new User(
                "user",
                "password",
                List.of(
                        new SimpleGrantedAuthority("ROLE_USER"),
                        new SimpleGrantedAuthority("ROLE_ADMIN")
                )
        );

        String token = jwtProvider.createToken(userDetails);

        // when
        Authentication authentication = jwtProvider.getAuthentication(token);

        // then
        assertThat(authentication).isNotNull();
        assertThat(authentication.getName()).isEqualTo("user");

        assertThat(authentication.getAuthorities())
                .extracting("authority")
                .containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN");
    }
}
