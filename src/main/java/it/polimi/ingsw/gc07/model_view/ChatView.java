package it.polimi.ingsw.gc07.model_view;

import it.polimi.ingsw.gc07.model.chat.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatView {
    /**
     * List containing chatMessages received by a player.
     */
    private final List<ChatMessage> chatMessages;

    /**
     * Constructor method for ChatView.
     */
    public ChatView() {
        this.chatMessages = new ArrayList<>();
    }

    /**
     * Method to add a new chatMessage to the chat view.
     * @param chatMessage new received chatMessage
     */
    public void addMessage(ChatMessage chatMessage) {
        chatMessages.add(chatMessage);
        System.out.println("You received a messxage: ");
        System.out.println(chatMessage.getContent());
    }
}
