package org.example.securityapp.jwt;

import org.example.securityapp.auth.JwtProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtFilterTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    JwtProvider jwtProvider;

    @Test
    @DisplayName("유효 토큰 발행")
    void testJwtFilter_success() throws Exception {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "user",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        given(jwtProvider.validateToken(anyString()))
                .willReturn(true);

        given(jwtProvider.getAuthentication(anyString()))
                .willReturn(authentication);

        mockMvc.perform(
                get("/test/secure")
                        .header("Authorization", "Bearer valid-token")
        )
                .andExpect(status().isOk())
                .andExpect(content().string("SECURE"));
    }

    @Test
    @DisplayName("토큰 없을 경우 접근 차단")
    void fail_testJwtFilter() throws Exception {
        mockMvc.perform(get("/test/secure"))
                .andExpect(status().isUnauthorized());
    }
}
