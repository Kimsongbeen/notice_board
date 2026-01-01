package org.example.securityapp.user;

import org.example.securityapp.auth.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController
{

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> register(@RequestBody LoginRequest request){
        Long userId = userService.register(
                request.getEmail(),
                request.getPassword()
        );
        return ResponseEntity.ok(userId);
    }
}
