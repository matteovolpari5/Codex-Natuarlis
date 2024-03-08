package it.polimi.ingsw.gc07.exceptions;

public class PlayerNotPresentExcpetion extends Exception{
    public PlayerNotPresentExcpetion() {
    }

    public PlayerNotPresentExcpetion(String message) {
        super(message);
    }

    public PlayerNotPresentExcpetion(String message, Throwable cause) {
        super(message, cause);
    }

    public PlayerNotPresentExcpetion(Throwable cause) {
        super(cause);
    }

    public PlayerNotPresentExcpetion(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
