package it.polimi.ingsw.gc07.exceptions;

public class IndexesOutOfGameFieldException extends Exception {
    public IndexesOutOfGameFieldException() {
    }

    public IndexesOutOfGameFieldException(String message) {
        super(message);
    }

    public IndexesOutOfGameFieldException(String message, Throwable cause) {
        super(message, cause);
    }

    public IndexesOutOfGameFieldException(Throwable cause) {
        super(cause);
    }

    public IndexesOutOfGameFieldException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
