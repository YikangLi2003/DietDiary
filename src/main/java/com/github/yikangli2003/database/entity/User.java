package com.github.yikangli2003.database.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class User {
    @Id
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


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

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

    public void storeUploadedFood(Food food) {
        uploadedFoods.add(food);
    }
}
