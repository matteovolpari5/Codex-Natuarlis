package it.polimi.ingsw.gc07.exceptions;

public class CardNotPlaceableException extends Exception{
    public CardNotPlaceableException() {
    }

    public CardNotPlaceableException(String message) {
        super(message);
    }

    public CardNotPlaceableException(String message, Throwable cause) {
        super(message, cause);
    }

    public CardNotPlaceableException(Throwable cause) {
        super(cause);
    }

    public CardNotPlaceableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
