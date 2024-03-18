package it.polimi.ingsw.gc07.exceptions;

/**
 * Exception that occurs when a player wants to send a message to an invalid receiver.
 * The exception may occur if the receiver is null or does not represent a player in the game.
 */
public class InvalidReceiverException extends Exception {
    public InvalidReceiverException() {
    }

    public InvalidReceiverException(String message) {
        super(message);
    }

    public InvalidReceiverException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidReceiverException(Throwable cause) {
        super(cause);
    }

    public InvalidReceiverException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
