package org.example.securityapp.security.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler
implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception)
            throws IOException, ServletException {

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String message;

        if(exception instanceof LockedException){
            message = "계정이 잠겼습니다. 관리자에게 문의하세요.";
        }else if(exception instanceof BadCredentialsException){
            message = "아이디 또는 비밀번호가 올바르지 않습니다.";
        }else if(exception instanceof DisabledException){
            message = "비활성화된 계정입니다.";
        }else{
            message = "인증에 실패했습니다.";
        }

        response.getWriter().write(
                "{\"message\":\"" + message + "\"}"
        );

    }
}
