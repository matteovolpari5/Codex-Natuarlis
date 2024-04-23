package it.polimi.ingsw.gc07.listeners;

import it.polimi.ingsw.gc07.model.chat.ChatMessage;

public interface ChatListener {
    /**
     * Method used to notify the player he has received a new chat chatMessage.
     * @param chatMessage new chatMessage
     */
    void showNewMessage(ChatMessage chatMessage);
}
