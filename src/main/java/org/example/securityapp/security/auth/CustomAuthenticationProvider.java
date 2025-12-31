package org.example.securityapp.security.auth;

import org.example.securityapp.security.service.LoginFailService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider
implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final LoginFailService loginFailService;

    public CustomAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder, LoginFailService loginFailService) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.loginFailService = loginFailService;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if(!userDetails.isAccountNonLocked()){
            throw new LockedException("Account is locked");
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())) {
            loginFailService.handleFail(username);

            if(!userDetails.isAccountNonLocked()){
                throw new LockedException("Account is locked");
            }

            throw new BadCredentialsException("Bad credentials");
        }

        loginFailService.handleSuccess(username);
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class
                .isAssignableFrom(authentication);
    }
}
