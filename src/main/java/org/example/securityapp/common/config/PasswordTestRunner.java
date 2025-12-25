package org.example.securityapp.common.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordTestRunner implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;

    public PasswordTestRunner(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        String encoded = passwordEncoder.encode("1234");
        System.out.println("BCrypt password = " + encoded);
    }
}
