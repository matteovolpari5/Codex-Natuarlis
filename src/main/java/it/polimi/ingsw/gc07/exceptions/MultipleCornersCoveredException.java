package it.polimi.ingsw.gc07.exceptions;

public class MultipleCornersCoveredException extends Exception {
    public MultipleCornersCoveredException() {
    }

    public MultipleCornersCoveredException(String message) {
        super(message);
    }

    public MultipleCornersCoveredException(String message, Throwable cause) {
        super(message, cause);
    }

    public MultipleCornersCoveredException(Throwable cause) {
        super(cause);
    }

    public MultipleCornersCoveredException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
