package com.github.yikangli2003.database.entity;

import jakarta.persistence.*;
import jakarta.persistence.GenerationType.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String hashedPassword;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime localRegistrationTime;

    @OneToMany(mappedBy = "uploader")
    private List<Food> uploadedFoods;

    public User(String email, String hashedPassword, String name, LocalDateTime localRegistrationTime) {
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.name = name;
        this.localRegistrationTime = localRegistrationTime;
    }

    public String getId() { return id; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String account) { this.email = email; }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getLocalRegistrationTime() {
        return localRegistrationTime;
    }

    public List<Food> getUploadedFoods() {
        return uploadedFoods;
    }
}
