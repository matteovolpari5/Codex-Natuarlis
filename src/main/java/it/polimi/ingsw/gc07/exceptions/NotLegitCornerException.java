package it.polimi.ingsw.gc07.exceptions;

public class NotLegitCornerException extends Exception {
    public NotLegitCornerException() {
    }

    public NotLegitCornerException(String message) {
        super(message);
    }

    public NotLegitCornerException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotLegitCornerException(Throwable cause) {
        super(cause);
    }

    public NotLegitCornerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
