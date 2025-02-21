package com.github.YikangLi2003;

import com.github.YikangLi2003.database.DatabaseAccessor;
import com.github.YikangLi2003.database.exception.DuplicateAccountException;
import org.example.database.exception.*;
import com.github.YikangLi2003.utils.PasswordUtils;

public class Main {
    public static void main(String[] args) {
        testDatabaseUsers();
    }

    private static void testDatabaseUsers() {
        DatabaseAccessor.initialize();

        try {
            DatabaseAccessor.insertUser("john1988", PasswordUtils.hashPassword("abcd1234"), "John Smith");
            DatabaseAccessor.insertUser("superjoe12", PasswordUtils.hashPassword("supp123"), "Joe Green");
        } catch (DuplicateAccountException e) {
            System.out.println("Duplicate account: " + e.getMessage());
        }

        System.out.println(DatabaseAccessor.showAllUsers());
    }
}