package it.polimi.ingsw.gc07.model_view_listeners;

import it.polimi.ingsw.gc07.model.chat.ChatMessage;

public interface ChatViewListener {
    /**
     * Method used to notify the player he has received a new chat chatMessage.
     * @param chatMessage chat message
     */
    void receiveMessageUpdate(ChatMessage chatMessage);
}
