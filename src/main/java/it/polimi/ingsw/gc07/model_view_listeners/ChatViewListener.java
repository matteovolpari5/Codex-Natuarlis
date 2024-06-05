package it.polimi.ingsw.gc07.model_view_listeners;

import it.polimi.ingsw.gc07.model.chat.ChatMessage;

import java.util.List;

/**
 * Listener of the client's Chat copy.
 */
public interface ChatViewListener {
    /**
     * Method used to notify the player he has received a new chat chatMessage.
     * @param chatMessage chat message
     */
    void receiveMessageUpdate(ChatMessage chatMessage);

    /**
     * Method used to notify the player he has received a full chat update.
     * @param chatMessages full chat update
     */
    void receiveFullChatUpdate(List<ChatMessage> chatMessages);
}
