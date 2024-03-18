package it.polimi.ingsw.gc07.exceptions;

public class NonValidScoreException extends Exception {
    public NonValidScoreException() {
    }

    public NonValidScoreException(String message) {
        super(message);
    }

    public NonValidScoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonValidScoreException(Throwable cause) {
        super(cause);
    }

    public NonValidScoreException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
