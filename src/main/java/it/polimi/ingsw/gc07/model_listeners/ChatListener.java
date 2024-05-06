package it.polimi.ingsw.gc07.model_listeners;

import it.polimi.ingsw.gc07.updates.ChatMessageUpdate;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ChatListener extends Remote {
    /**
     * Method used to notify the player he has received a new chat chatMessage.
     * @param chatMessageUpdate chat message update
     */
    void receiveChatMessageUpdate(ChatMessageUpdate chatMessageUpdate) throws RemoteException;
}
