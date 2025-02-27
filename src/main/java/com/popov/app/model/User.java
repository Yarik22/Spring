package com.popov.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Entity
@Table(name = "\"Users\"")
public class User {
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @NotBlank(message = "Email is required")
    @Column(name = "username")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Column(name = "password")
    private String password;

    public User() {
        this.id = UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }


    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public User(String username, String password) {

        this.username = username;
        this.password = password;
    }
}