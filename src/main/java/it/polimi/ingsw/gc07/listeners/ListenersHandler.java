package it.polimi.ingsw.gc07.listeners;

import java.util.ArrayList;
import java.util.List;

public class ListenersHandler {
    private final List<GameListener> listeners;

    public ListenersHandler() {
        this.listeners = new ArrayList<>();
    }

    public void addListener(GameListener listener) {
        listeners.add(listener);
    }

}
