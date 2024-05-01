package it.polimi.ingsw.gc07.exceptions;

/**
 * Exception meaning that there is not a correct number of players.
 */
public class WrongNumberOfPlayersException extends Exception {
    public WrongNumberOfPlayersException() {
    }

    public WrongNumberOfPlayersException(String message) {
        super(message);
    }

    public WrongNumberOfPlayersException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongNumberOfPlayersException(Throwable cause) {
        super(cause);
    }

    public WrongNumberOfPlayersException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
