package org.example.securityapp.security.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.securityapp.user.User;
import org.example.securityapp.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class LoginFailService {

    private final UserRepository userRepository;

    public LoginFailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void handleFail(String username){
        userRepository.findByUsername(username)
                .ifPresent(user -> {
                    user.increaseFailCount();

                    if(user.isLockRequired()){
                        user.lock();
                    }
                });
    }

    public void handleSuccess(String username){
        userRepository.findByUsername(username)
                .ifPresent(User::resetFailCount);
    }
}
