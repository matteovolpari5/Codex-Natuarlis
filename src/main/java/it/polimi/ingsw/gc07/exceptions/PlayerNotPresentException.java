package it.polimi.ingsw.gc07.exceptions;

/**
 * Exception meaning that the specified player is not present.
 */
public class PlayerNotPresentException extends Exception{
    public PlayerNotPresentException() {}

    public PlayerNotPresentException(String message) {
        super(message);
    }

    public PlayerNotPresentException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlayerNotPresentException(Throwable cause) {
        super(cause);
    }

    public PlayerNotPresentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
