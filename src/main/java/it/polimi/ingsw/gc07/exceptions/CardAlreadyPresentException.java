package it.polimi.ingsw.gc07.exceptions;

/**
 * Exception telling that a card is already present in the given place.
 * A new card can't be placed in the given place.
 */
public class CardAlreadyPresentException extends Exception {
    public CardAlreadyPresentException() {
    }

    public CardAlreadyPresentException(String message) {
        super(message);
    }

    public CardAlreadyPresentException(String message, Throwable cause) {
        super(message, cause);
    }

    public CardAlreadyPresentException(Throwable cause) {
        super(cause);
    }

    public CardAlreadyPresentException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
