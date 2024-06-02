package it.polimi.ingsw.gc07.utils;

public abstract class SafePrinter {
    public static synchronized void print(String string) {
        System.out.print(string);
    }
    public static synchronized void println(String string) {
        System.out.println(string);
    }
}
