package org.example.securityapp.user;

import jakarta.transaction.Transactional;
import org.example.securityapp.user.repository.RoleRepository;
import org.example.securityapp.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public Long register(String email, String password) {

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(()-> new IllegalStateException("ROLE_USER not found"));

        User user = User.createUser(
                email, password);

        user.addRole(userRole);

        userRepository.save(user);
        return user.getId();
    }
}
