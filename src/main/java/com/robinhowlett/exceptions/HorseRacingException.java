package com.robinhowlett.exceptions;

public abstract class HorseRacingException extends Exception {

    public HorseRacingException() {
    }

    public HorseRacingException(String message) {
        super(message);
    }

    public HorseRacingException(String message, Throwable cause) {
        super(message, cause);
    }
}
