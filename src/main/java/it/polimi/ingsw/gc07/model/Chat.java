package it.polimi.ingsw.gc07.model;

import it.polimi.ingsw.gc07.exceptions.EmptyChatException;
import it.polimi.ingsw.gc07.exceptions.InvalidReceiverException;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    /**
     * List containing the messages sent to players in the game.
     * It contains bought public and private messages.
     */
    private List<Message> messages;

    /**
     * Constructor method for Chat.
     */
    public Chat() {
        this.messages = new ArrayList<>();
    }

    /**
     * Method to add a new message to the chat.
     * @param content Content
     * @param sender Sender nickname
     * @param isPublic Boolean value, tells if the message is public (true) or not (false)
     * @param receiver Receiver nickname
     * @param players List of players in the game
     * @throws InvalidReceiverException
     */
    public void addMessage(String content, String sender, boolean isPublic, String receiver, List<String> players) throws InvalidReceiverException {
        if(isPublic){
            // create new public message
            messages.add(new Message(content, sender, isPublic));
        }
        else{
            // create new private message
            if(receiver == null || players == null || !players.contains(receiver)){
                throw new InvalidReceiverException();
            }
            messages.add(new PrivateMessage(content, sender, isPublic, receiver));
        }
    }

    public Message getLastMessage(String receiver) throws EmptyChatException {
        if(messages.size() < 1){
            throw new EmptyChatException();
        }
        return getContent(receiver).getLast();
    }

    /**
     * Method that return the whole chat for a given receiver,
     * containing public messages and private messages for the receiver.
     * @param receiver
     * @return
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

