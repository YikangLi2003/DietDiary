package com.github.YikangLi2003.database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class User {
    @Id
    private String account;

    @NotNull
    private String hashedPassword;

    @NotNull
    private String name;

    @NotNull
    private final LocalDateTime localRegistrationTime;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<@NotNull Food> foods;

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

    public List<Food> getFoods() {
        return foods;
    }


    public void setFoods(List<Food> foods) {
        this.foods = foods;
        for (Food food : foods) {
            food.setUser(this);
        }
    }

    public void setFood(Food food) {
        foods.add(food);
        food.setUser(this);
    }

    public void removeFood(Food food) {
        foods.remove(food);
        food.setUser(null);
    }
}
