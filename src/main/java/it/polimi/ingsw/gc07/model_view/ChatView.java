package it.polimi.ingsw.gc07.model_view;

import it.polimi.ingsw.gc07.model.chat.ChatMessage;
import it.polimi.ingsw.gc07.model_view_listeners.ChatViewListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Client's copy of model Chat.
 */
public class ChatView {
    /**
     * List containing chatMessages received by a player.
     */
    private final List<ChatMessage> chatMessages;
    /**
     * List of chat view listeners.
     */
    private final List<ChatViewListener> chatViewListeners;

    /**
     * Constructor method for ChatView.
     */
    public ChatView() {
        this.chatMessages = new ArrayList<>();
        this.chatViewListeners = new ArrayList<>();
    }

    /**
     * Method to register a chat view listener.
     * @param chatViewListener chat view listener
     */
    public void addListener(ChatViewListener chatViewListener) {
        chatViewListeners.add(chatViewListener);
    }

    /**
     * Method to add a new chatMessage to the chat view.
     * @param chatMessage new received chatMessage
     */
    public void addMessage(ChatMessage chatMessage) {
        chatMessages.add(chatMessage);
        if (chatMessage == null) {
            return;
        }
        for(ChatViewListener chatViewListener: chatViewListeners) {
            chatViewListener.receiveMessageUpdate(chatMessage);
        }
    }

    /**
     * Method to add all chat messages after a reconnection.
     * @param chatMessages full chat content
     */
    public void setChatMessages(List<ChatMessage> chatMessages) {
        if (chatMessages == null) {
            return;
        }
        this.chatMessages.addAll(chatMessages);
        for(ChatViewListener chatViewListener: chatViewListeners) {
            chatViewListener.receiveFullChatUpdate(chatMessages);
        }
    }

    /**
     * Method that returns all chat messages.
     * @return chat messages
     */
    public List<ChatMessage> getMessages() {
        return new ArrayList<>(chatMessages);
    }
}
