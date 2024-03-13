package it.polimi.ingsw.gc07.exceptions;
/**
 * Exception meaning that there is not a correct number of players.
 */
public class WrongPlayerException extends Exception {
    public WrongPlayerException() {
    }

    public WrongPlayerException(String message) {
        super(message);
    }

    public WrongPlayerException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongPlayerException(Throwable cause) {
        super(cause);
    }

    public WrongPlayerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
