package com.github.yikangli2003.database.exception;

public class DuplicatedEntityPropertyException extends DataAccessException {
    public DuplicatedEntityPropertyException(
            String entityName,
            String propertyName,
            String duplicatedValue,
            Throwable cause
    ) {
        super(
                String.format(
                        "Property '%s' valued '%s' is already in use by another entity '%s'.",
                        propertyName, duplicatedValue, entityName
                ),
                cause
        );
    }
}
