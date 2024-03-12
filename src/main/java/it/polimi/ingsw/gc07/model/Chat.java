package it.polimi.ingsw.gc07.model;


import java.util.ArrayList;
import java.util.List;

public class Chat {
    private List<String> content;

    public Chat() {
        this.content = new ArrayList<>();
    }

    public void addMessage(String newMessage) {
        this.content.add(newMessage);
    }

    public List<String> getContent() {
        return new ArrayList<>(content);
    }
}

