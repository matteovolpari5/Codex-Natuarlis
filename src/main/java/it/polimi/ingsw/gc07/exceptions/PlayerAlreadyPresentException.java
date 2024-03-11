package it.polimi.ingsw.gc07.exceptions;

public class PlayerAlreadyPresentException extends Exception {
    public PlayerAlreadyPresentException() {
    }

    public PlayerAlreadyPresentException(String message) {
        super(message);
    }

    public PlayerAlreadyPresentException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlayerAlreadyPresentException(Throwable cause) {
        super(cause);
    }

    public PlayerAlreadyPresentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
