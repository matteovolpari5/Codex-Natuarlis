package it.polimi.ingsw.gc07.exceptions;

public class EmptyChatException extends Exception {
    public EmptyChatException() {
    }

    public EmptyChatException(String message) {
        super(message);
    }

    public EmptyChatException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmptyChatException(Throwable cause) {
        super(cause);
    }

    public EmptyChatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
