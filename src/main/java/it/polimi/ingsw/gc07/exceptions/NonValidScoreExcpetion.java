package it.polimi.ingsw.gc07.exceptions;

public class NonValidScoreExcpetion extends Exception {
    public NonValidScoreExcpetion() {
    }

    public NonValidScoreExcpetion(String message) {
        super(message);
    }

    public NonValidScoreExcpetion(String message, Throwable cause) {
        super(message, cause);
    }

    public NonValidScoreExcpetion(Throwable cause) {
        super(cause);
    }

    public NonValidScoreExcpetion(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
