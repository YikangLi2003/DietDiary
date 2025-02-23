package com.github.yikangli2003.database.exception;

public class DuplicatedUserAccountException extends DataAccessException {
    public DuplicatedUserAccountException(String duplicatedAccount, Throwable cause) {
        super("Account name '" + duplicatedAccount + "' is already in use by another user.", cause);
    }
}
