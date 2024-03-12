package it.polimi.ingsw.gc07.exceptions;

public class WrongCardTypeException extends Exception {
    public WrongCardTypeException() {
    }

    public WrongCardTypeException(String message) {
        super(message);
    }

    public WrongCardTypeException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongCardTypeException(Throwable cause) {
        super(cause);
    }

    public WrongCardTypeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
