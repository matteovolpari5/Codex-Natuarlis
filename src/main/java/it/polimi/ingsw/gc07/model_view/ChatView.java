package it.polimi.ingsw.gc07.model_view;

import it.polimi.ingsw.gc07.model.chat.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatView {
    /**
     * List containing messages received by a player.
     */
    private final List<Message> messages;

    /**
     * Constructor method for ChatView.
     */
    public ChatView() {
        this.messages = new ArrayList<>();
    }

    /**
     * Method to add a new message to the chat view.
     * @param message new received message
     */
    public void addMessage(Message message) {
        messages.add(message);
    }

    // TODO meotodo per stampare la chat / ultimo messaggio
}
