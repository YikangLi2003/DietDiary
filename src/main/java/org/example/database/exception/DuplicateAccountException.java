package org.example.database.exception;

public class DuplicateAccountException extends Exception {
    public DuplicateAccountException(String account) {
        super("Account '" + account + "' already exists. Please try a different account name.");
    }
}