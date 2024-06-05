package it.polimi.ingsw.gc07.model_listeners;

import it.polimi.ingsw.gc07.updates.ChatMessageUpdate;
import it.polimi.ingsw.gc07.updates.FullChatUpdate;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Listener of the model Chat.
 */
public interface ChatListener extends Remote {
    /**
     * Method used to notify the player he has received a new chat chatMessage.
     * @param chatMessageUpdate chat message update
     */
    void receiveChatMessageUpdate(ChatMessageUpdate chatMessageUpdate) throws RemoteException;
    /**
     * Method used to notify the player the full content of the chat after a reconnection.
     * @param fullChatUpdate full message update
     */
    void receiveFullChatUpdate(FullChatUpdate fullChatUpdate) throws RemoteException;
}
