package it.polimi.ingsw.gc07.listeners;

import it.polimi.ingsw.gc07.updates.ChatMessageUpdate;

public interface ChatListener {
    /**
     * Method used to notify the player he has received a new chat chatMessage.
     * @param chatMessageUpdate chat message update
     */
    void receiveChatMessageUpdate(ChatMessageUpdate chatMessageUpdate);
    // TODO l'implementazione è chatMessageUpdate.execute
}
