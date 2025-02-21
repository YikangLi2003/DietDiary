package com.github.YikangLi2003;

import com.github.YikangLi2003.database.DatabaseAccessor;
import com.github.YikangLi2003.database.exception.DuplicateAccountException;
import com.github.YikangLi2003.database.entity.*;
import com.github.YikangLi2003.utils.PasswordUtils;

public class Main {
    public static void main(String[] args) {
        testDatabaseUsers();
    }

    private static void testDatabaseUsers() {
        User user1 = new User("user1", PasswordUtils.hashPassword("password1"), null, null);
        if (user1.getName() == null) {
            System.out.println("User name is null.");
        }
    }
}