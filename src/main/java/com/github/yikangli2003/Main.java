package com.github.yikangli2003;

import com.github.yikangli2003.database.DataAccessor;
import com.github.yikangli2003.database.entity.*;
import com.github.yikangli2003.database.exception.*;
import com.github.yikangli2003.util.*;

import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        /*DataAccessor.signUpNewUser(
                "user1",
                PasswordUtil.hashPassword("12345"),
                "Bob",
                LocalDateTime.now()
        );

        DataAccessor.signUpNewUser(
                "user2",
                PasswordUtil.hashPassword("67890"),
                "Alice",
                LocalDateTime.now()
        );*/

        try {
            User user = DataAccessor.getUserByAccount("user1");
            System.out.println("Find user " + user.getName() + ".");
            user = DataAccessor.getUserByAccount("user2");
            System.out.println("Find user " + user.getName() + ".");
            user = DataAccessor.getUserByAccount("user3");
            System.out.println("Find user " + user.getName() + ".");
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
        }

        User user = DataAccessor.getUserByAccount("user1");
        if (PasswordUtil.checkPassword("12345", user.getHashedPassword())) {
            System.out.println(user.getName() + " has password 12345 and hasded as " + user.getHashedPassword());
        } else {
            System.out.println("Password incorrect.");
        }

        try {
            DataAccessor.changeUserPassword("user1", PasswordUtil.hashPassword("18273"));
            DataAccessor.changeUserPassword("user2", PasswordUtil.hashPassword("99887"));
            DataAccessor.changeUserPassword("user3", PasswordUtil.hashPassword("73641"));
        } catch (UserNotFoundException e) {
            System.out.println(e);
        }

        user = DataAccessor.getUserByAccount("user1");
        if (PasswordUtil.checkPassword("12345", user.getHashedPassword())) {
            System.out.println(user.getName() + " has password 12345 and hasded as " + user.getHashedPassword());
        } else if (PasswordUtil.checkPassword("18273", user.getHashedPassword())) {
            System.out.println(user.getName() + " has password 18273 and hasded as " + user.getHashedPassword());
        }



        DataAccessor.closeEntityManagerFactory();
    }
}