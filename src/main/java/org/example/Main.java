package org.example;

import org.example.database.DatabaseAccessor;
import org.example.database.exception.*;

public class Main {
    public static void main(String[] args) {
        testDatabaseUsers();
    }

    private static void testDatabaseUsers() {
        DatabaseAccessor.initialize();

        try {
            DatabaseAccessor.addUser("john1988", PasswordUtils.hashPassword("abcd1234"), "John Smith");
            DatabaseAccessor.addUser("superjoe12", PasswordUtils.hashPassword("supp123"), "Joe Green");
        } catch (DuplicateAccountException e) {
            System.out.println("Duplicate account: " + e.getMessage());
        }

        System.out.println(DatabaseAccessor.showAllUsers());
    }
}