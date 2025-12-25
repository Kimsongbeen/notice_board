package org.example.securityapp.user;

import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class Role {

    protected Role(){}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // ROLE_USER, ROLE_ADMIN

    public Role(String name){
        this.name = name;
    }
}
