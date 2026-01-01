package org.example.securityapp.user;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    /* ==============================
       생성자
     ============================== */
    protected User() {
    }

    /* ==============================
       PK
     ============================== */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ==============================
       핵심 식별 정보
     ============================== */
    @Column(nullable = false, unique = true)
    private String email;

    /* ==============================
       보안 관련 필드
     ============================== */
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE;

    private int failCount;

    private boolean twoFactorEnabled;

    private LocalDateTime lastLoginAt;

    /* ==============================
       권한 연관관계
     ============================== */
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<UserRole> userRoles = new ArrayList<>();

    /* ==============================
       도메인 행위 (보안 중심)
     ============================== */
    public void increaseFailCount() {
        this.failCount++;
    }

    public void resetFailCount() {
        this.failCount = 0;
    }

    public boolean isLockRequired() {
        return this.failCount >= 5;
    }

    public void lock() {
        this.status = UserStatus.LOCKED;
    }

    public void updateLastLoginAt() {
        this.lastLoginAt = LocalDateTime.now();
    }

    public void addRole(Role role) {
        UserRole userRole = new UserRole(this, role);
        this.userRoles.add(userRole);
    }

    /* ==============================
       팩토리 메서드
     ============================== */
    public static User createUser(
            String email,
            String encodedPassword
    ) {
        User user = new User();
        user.email = email;
        user.password = encodedPassword;
        user.status = UserStatus.ACTIVE;
        user.failCount = 0;
        user.twoFactorEnabled = false;
        return user;
    }

    /* ==============================
       Getter
     ============================== */
    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UserStatus getStatus() {
        return status;
    }

    public int getFailCount() {
        return failCount;
    }

    public boolean isTwoFactorEnabled() {
        return twoFactorEnabled;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public List<UserRole> getUserRoles() {
        return userRoles;
    }
}