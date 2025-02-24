package com.github.yikangli2003.database.exception;

public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException(String absentAccount) {
      super("There is no user with account '" + absentAccount + "' in the database.");
  }
}
