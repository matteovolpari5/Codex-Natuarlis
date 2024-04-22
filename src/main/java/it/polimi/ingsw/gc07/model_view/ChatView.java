package it.polimi.ingsw.gc07.model_view;

import it.polimi.ingsw.gc07.model.chat.Message;
import it.polimi.ingsw.gc07.model.chat.PrivateMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatView {
    /**
     * List containing the messages sent to players in the game.
     * It contains both public and private messages.
     */
    private final List<Message> messages;

    /**
     * Constructor method for ChatView.
     */
    public ChatView() {
        this.messages = new ArrayList<>();
    }

    /**
     * Method to add a new public message to the chat.
     * @param content content of the message
     * @param sender sender nickname
     */
    public void addPublicMessage(String content, String sender) {
        messages.add(new Message(content, sender, true));
    }

    /**
     * Method to add a new public message to the chat.
     * @param content content of the message
     * @param sender sender nickname
     * @param receiver receiver nickname
     */
    public void addPrivateMessage(String content, String sender, String receiver) {
        messages.add(new PrivateMessage(content, sender, false, receiver));
    }
}
