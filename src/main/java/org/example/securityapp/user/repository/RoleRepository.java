package org.example.securityapp.user.repository;

import org.example.securityapp.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository {
    Optional<Role> findByName(String name);
}
