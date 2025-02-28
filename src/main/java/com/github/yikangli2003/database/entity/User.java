package com.github.yikangli2003.database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String account;

    @Column(nullable = false)
    private String hashedPassword;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime localRegistrationTime;

    @OneToMany(mappedBy = "uploader")
    private List<Food> uploadedFoods;

    public User(String account, String hashedPassword, String name, LocalDateTime localRegistrationTime) {
        this.account = account;
        this.hashedPassword = hashedPassword;
        this.name = name;
        this.localRegistrationTime = localRegistrationTime;
    }

    public Long getId() { return id; }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) { this.account = this.account; }

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
