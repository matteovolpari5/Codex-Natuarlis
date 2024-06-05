package it.polimi.ingsw.gc07.utils;

/**
 * Class used to implement safe (synchronized) print methods.
 */
public abstract class SafePrinter {
    /**
     * Method used to print a string on the same line.
     * This method ensures thread safety.
     * @param string string to print
     */
    public static synchronized void print(String string) {
        System.out.print(string);
    }

    /**
     * Method used to print a string on a new line.
     * This method ensures thread safety.
     * @param string string to print
     */
    public static synchronized void println(String string) {
        System.out.println(string);
    }
}
