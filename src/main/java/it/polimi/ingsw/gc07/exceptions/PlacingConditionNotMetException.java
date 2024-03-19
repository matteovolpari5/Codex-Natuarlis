package it.polimi.ingsw.gc07.exceptions;

public class PlacingConditionNotMetException extends Exception {
    public PlacingConditionNotMetException() {
    }

    public PlacingConditionNotMetException(String message) {
        super(message);
    }

    public PlacingConditionNotMetException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlacingConditionNotMetException(Throwable cause) {
        super(cause);
    }

    public PlacingConditionNotMetException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
