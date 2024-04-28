package it.polimi.ingsw.gc07.model.chat;

import it.polimi.ingsw.gc07.listeners.ChatListener;
import it.polimi.ingsw.gc07.updates.ChatMessageUpdate;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the chat of the game.
 */
public class Chat {
    /**
     * List containing the chatMessages sent to players in the game.
     * It contains both public and private chatMessages.
     */
    private final List<ChatMessage> chatMessages;
    /**
     * List of chat listeners.
     */
    private final List<ChatListener> chatListeners;

    /**
     * Constructor method for Chat.
     */
    public Chat() {
        this.chatMessages = new ArrayList<>();
        this.chatListeners = new ArrayList<>();
    }

    /**
     * Method to add a chat listener.
     * @param chatListener new chat lister
     */
    public void addListener(ChatListener chatListener) {
        chatListeners.add(chatListener);
    }

    /**
     * Method to add a new public message to the chat.
     * @param content content of the message
     * @param sender sender nickname
     */
    public void addPublicMessage(String content, String sender) {
        // create and add message
        ChatMessage newMessage = new ChatMessage(content, sender, true);
        chatMessages.add(newMessage);
        // inform listeners
        for(ChatListener l: chatListeners) {
            try {
                l.receiveChatMessageUpdate(new ChatMessageUpdate(newMessage));
            }catch(RemoteException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
    }

    /**
     * Method to add a new public message to the chat.
     * @param content content of the message
     * @param sender sender nickname
     * @param receiver receiver nickname
     */
    public void addPrivateMessage(String content, String sender, String receiver) {
        // create and add message
        PrivateChatMessage newMessage = new PrivateChatMessage(content, sender, false, receiver);
        chatMessages.add(newMessage);
        // inform listeners

        ChatMessageUpdate update = new ChatMessageUpdate(newMessage);
        for(ChatListener l: chatListeners) {
            try {
                l.receiveChatMessageUpdate(update);
            }catch(RemoteException e) {
                e.printStackTrace();
                throw new RuntimeException();
            }
        }
    }

    /**
     * Method returning the last message for the specified receiver.
     * Returns null if the receiver has no chatMessages.
     * @param receiver receiver nickname
     * @return last message
     */
    public ChatMessage getLastMessage(String receiver) {
        if(!getContent(receiver).isEmpty()) {
            // chatMessages are immutable
            return getContent(receiver).getLast();
        }
        return null;
    }

    /**
     * Method that returns the whole chat for a given receiver,
     * containing public chatMessages and private chatMessages for the receiver.
     * @param receiver nickname of the receiver
     * @return chatMessages the receiver has received
     */
    public List<ChatMessage> getContent(String receiver) {
        List<ChatMessage> receiverChatMessages = new ArrayList<>();
        for(ChatMessage m: chatMessages){
            if(m.isForReceiver(receiver)){
                // chatMessages are immutable
                receiverChatMessages.add(m);
            }
        }
        return receiverChatMessages;
    }
}

