package com.fiap.techchallenge14.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String password;
    private String address;
    private String login;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdatedAt;
    private LocalDateTime lastLoginAt;
    private Boolean active;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity role;
}
