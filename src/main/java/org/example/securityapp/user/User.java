package org.example.securityapp.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserStatus getStatus() {
        return status;
    }

    public List<UserRole> getUserRoles() {
        return userRoles;
    }

    public int getFailCount() {
        return failCount;
    }

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRole> userRoles = new ArrayList<>();

    private int failCount;

    public void increaseFailCount() {
        this.failCount++;
    }

    public void resetFailCount() {
        this.failCount = 0;
    }

    public boolean isLockRequired(){
        return this.failCount >= 5;
    }

    public void lock(){
        this.status = UserStatus.LOCKED;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
