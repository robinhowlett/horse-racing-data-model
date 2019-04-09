package com.robinhowlett.exceptions;

public abstract class DataModelException extends HorseRacingException {

    public DataModelException(String message) {
        super(message);
    }

    public DataModelException(String message, Throwable cause) {
        super(message, cause);
    }
}
