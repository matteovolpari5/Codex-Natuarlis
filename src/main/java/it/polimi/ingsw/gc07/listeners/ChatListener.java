package it.polimi.ingsw.gc07.listeners;

import it.polimi.ingsw.gc07.model.chat.Message;

public interface ChatListener {
    /**
     * Method used to notify the player he has received a new chat message.
     * @param message new message
     */
    void showNewMessage(Message message);
}
