package org.example.securityapp.controller;

import org.example.securityapp.auth.JwtProvider;
import org.example.securityapp.user.controller.ReissueController;
import org.example.securityapp.user.service.RefreshTokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(ReissueController.class)
class ReissueControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    JwtProvider jwtProvider;

    @MockitoBean
    RefreshTokenService refreshTokenService;

    @Test
    @DisplayName("Refresh Token으로 Access Token 재발급 성공")
    void reissue_success() throws Exception {

        given(jwtProvider.getSubject("refresh-token"))
                .willReturn("user1");

        given(refreshTokenService.validRefreshToken("user1", "refresh-token"))
                .willReturn(true);

        UserDetails userDetails =
                new User("user1", "", List.of());

        given(jwtProvider.getUserDetails("refresh-token"))
                .willReturn(userDetails);

        given(jwtProvider.createToken(userDetails))
                .willReturn("new-access-token");

        given(jwtProvider.createRefreshToken(userDetails))
                .willReturn("new-refresh-token");

        mockMvc.perform(
                        post("/auth/reissue")
                                .with(csrf())
                                .header("Authorization", "Bearer refresh-token")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken")
                        .value("new-access-token"))
                .andExpect(jsonPath("$.refreshToken")
                        .value("new-refresh-token"));
    }

    @Test
    @DisplayName("Refresh Token 없으면 401")
    void reissue_fail_no_token() throws Exception {
        mockMvc.perform(post("/auth/reissue")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }
}

