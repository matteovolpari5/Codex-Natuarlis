package it.polimi.ingsw.gc07.exceptions;

/**
 * Exception meaning that there is not the required card.
 */
public class CardNotPresentException extends Exception {
    public CardNotPresentException() {
    }

    public CardNotPresentException(String message) {
        super(message);
    }

    public CardNotPresentException(String message, Throwable cause) {
        super(message, cause);
    }

    public CardNotPresentException(Throwable cause) {
        super(cause);
    }

    public CardNotPresentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
