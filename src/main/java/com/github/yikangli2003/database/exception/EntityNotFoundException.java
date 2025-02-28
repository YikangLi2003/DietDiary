package com.github.yikangli2003.database.exception;

public class EntityNotFoundException extends DataAccessException {
  public EntityNotFoundException(String entityName, String keyPropertyName, String keyValue) {
      super(String.format("There is no entity '%s' with property '%s' valued '%s'.", entityName, keyPropertyName, keyValue));
  }
}
