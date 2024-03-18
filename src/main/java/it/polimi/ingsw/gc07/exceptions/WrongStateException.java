package it.polimi.ingsw.gc07.exceptions;
/**
 * Exception meaning that the state of the game is wrong.
 */
public class WrongStateException extends Exception {
    public WrongStateException() {
    }

    public WrongStateException(String message) {
        super(message);
    }

    public WrongStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongStateException(Throwable cause) {
        super(cause);
    }

    public WrongStateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
