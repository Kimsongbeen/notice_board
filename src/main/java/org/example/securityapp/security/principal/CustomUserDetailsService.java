package org.example.securityapp.security.principal;

import org.example.securityapp.user.User;
import org.example.securityapp.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private static final Logger log =
            LoggerFactory.getLogger(CustomUserDetailsService.class);

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {


        log.info("loadUserByUsername 호출됨: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(()->
                        new UsernameNotFoundException("User not found"));

        return new CustomUserDetails(user);
    }
}
