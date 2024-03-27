package it.polimi.ingsw.gc07.model.chat;

import it.polimi.ingsw.gc07.exceptions.EmptyChatException;
import it.polimi.ingsw.gc07.exceptions.InvalidReceiverException;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    /**
     * List containing the messages sent to players in the game.
     * It contains bought public and private messages.
     */
    private final List<Message> messages;

    /**
     * Constructor method for Chat.
     */
    public Chat() {
        this.messages = new ArrayList<>();
    }

    /**
     * Method to add a new public message to the chat.
     * @param content content of the message
     * @param sender sender nickname
     * @param players list of players in the game
     */
    public void addPublicMessage(String content, String sender, List<String> players) {
        assert(players.contains(sender)): "The sender is not among the players";
        messages.add(new Message(content, sender, true));
    }

    /**
     * Method to add a new public message to the chat.
     * @param content content of the message
     * @param sender sender nickname
     * @param receiver receiver nickname
     * @param players list of players in the game
     */
    public void addPrivateMessage(String content, String sender, String receiver, List<String> players) {
        assert(players.contains(sender)): "The sender is not among the players";
        assert(players.contains(receiver)): "The receiver is not among the players";
        messages.add(new PrivateMessage(content, sender, false, receiver));
    }

    public Message getLastMessage(String receiver) throws EmptyChatException {
        if(messages.isEmpty()){
            throw new EmptyChatException();
        }
        return getContent(receiver).getLast();
    }

    /**
     * Method that returns the whole chat for a given receiver,
     * containing public messages and private messages for the receiver.
     * @param receiver nickname of the receiver
     * @return messages the receiver has received
     */
    public List<Message> getContent(String receiver) {
        List<Message> receiverMessages = new ArrayList<>();
        for(Message m: messages){
            if(m.isPublic() || (m.getReceiver()!=null && m.getReceiver().equals(receiver))){
                receiverMessages.add(m);
            }
        }
        return receiverMessages;
    }
}

