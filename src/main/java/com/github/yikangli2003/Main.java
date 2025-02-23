package com.github.yikangli2003;

import com.github.yikangli2003.database.DataAccessor;
import com.github.yikangli2003.database.entity.*;
import com.github.yikangli2003.util.*;

public class Main {
    public static void main(String[] args) {
        // sign up some random users with dummy information
        DataAccessor.signUpNewUser(
                "user1", PasswordUtil.hashPassword("asdf"), "Alice", java.time.LocalDateTime.now()
        );
        DataAccessor.signUpNewUser(
                "user2", PasswordUtil.hashPassword("asdf"), "Bob", java.time.LocalDateTime.now()
        );
        DataAccessor.signUpNewUser(
                "user3", PasswordUtil.hashPassword("asdf"), "Charlie", java.time.LocalDateTime.now()
        );

        for (User user : DataAccessor.getAllUsers()) {
            System.out.println(user.getAccount());
            System.out.println(user.getName());
            System.out.println(user.getHashedPassword());
            System.out.println(user.getLocalRegistrationTime());
            System.out.println("\n");
        }

        DataAccessor.closeEntityManagerFactory();

    }
}