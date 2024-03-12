package it.polimi.ingsw.gc07.model;


import it.polimi.ingsw.gc07.exceptions.EmptyChatException;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;

public class Chat {
    private List<String> content;

    public Chat() {
        this.content = new ArrayList<>();
    }

    public void addMessage(String newMessage) {
        this.content.add(newMessage);
    }

    public String getLastMessage() throws EmptyChatException {
        if(content.size() < 1){
            throw new EmptyChatException();
        }
        return content.get(content.size() - 1);
    }

    public List<String> getContent() {
        return new ArrayList<>(content);
    }

    // Poi dovremo capire come
}

