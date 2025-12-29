package org.example.securityapp.controller;

import org.example.securityapp.auth.JwtProvider;
import org.example.securityapp.common.config.SecurityConfig;
import org.example.securityapp.user.controller.LogoutController;
import org.example.securityapp.user.service.RefreshTokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class LogoutControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    JwtProvider jwtProvider;

    @MockitoBean
    RefreshTokenService refreshTokenService;

    @Test
    @DisplayName("로그아웃 성공")
    void logout_success() throws Exception {

        given(jwtProvider.getSubject("refresh-token"))
                .willReturn("user1");

        mockMvc.perform(
                        post("/auth/logout")
                                .with(csrf())
                                .header("Authorization", "Bearer refresh-token")
                )
                .andExpect(status().isOk());

        verify(refreshTokenService)
                .deleteRefreshToken("user1");
    }

}
