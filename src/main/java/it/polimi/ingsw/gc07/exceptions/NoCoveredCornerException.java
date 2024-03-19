package it.polimi.ingsw.gc07.exceptions;

public class NoCoveredCornerException extends Exception {
    public NoCoveredCornerException() {
    }

    public NoCoveredCornerException(String message) {
        super(message);
    }

    public NoCoveredCornerException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoCoveredCornerException(Throwable cause) {
        super(cause);
    }

    public NoCoveredCornerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
