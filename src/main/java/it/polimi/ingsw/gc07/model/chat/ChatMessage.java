package it.polimi.ingsw.gc07.model.chat;

import java.io.Serializable;
import java.util.Date;

/**
 * Class representing a message.
 * Public messages are instances of this class.
 * ChatMessage is immutable.
 */
public class ChatMessage implements Serializable {
    /**
     * String containing the body of the message.
     */
    private final String content;
    /**
     * String that identifies the sender by its unique nickname.
     */
    private final String senderNickname;
    /**
     * Date and time the message was sent.
     */
    private final Date dateTime;

    /**
     * Boolean attribute, that tells if the message is public.
     * If true, the message is public, i.e. visible by all the players in the same game.
     */
    private final boolean isPublic;

    /**
     * Constructor for ChatMessage.
     * @param content content of the message
     * @param senderNickname nickname of the sender
     * @param isPublic boolean value, tells if the message is public (true) or not (false)
     */
    public ChatMessage(String content, String senderNickname, boolean isPublic) {
        this.content = content;
        this.senderNickname = senderNickname;
        this.dateTime = new Date(System.currentTimeMillis());
        // can be formatted with SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy 'at' HH:mm:ss z");
        this.isPublic = isPublic;
    }

    /**
     * Getter for the ChatMessage's content (immutable).
     * @return content of the message
     */
    public String getContent(){
        return content;
    }

    /**
     * Getter for the ChatMessage's sender (immutable).
     * @return nickname of the ChatMessage's sender
     */
    public String getSender(){
        return senderNickname;
    }

    /**
     * Getter method for the date and time.
     * @return copy of the date and time
     */
    public Date getDateTime() {
        return new Date(dateTime.getTime());
    }

    /**
     * Getter method for isPublic attribute.
     * @return attribute isPublic
     */
    public boolean getIsPublic() {
        return isPublic;
    }
    /**
     * Method that checks if the message is for a certain receiver.
     * @param receiver nickname of the receiver
     * @return true if the message is for the receiver, false in other cases
     */
    public boolean isForReceiver(String receiver){
        return true;
    }

    /**
     * method to simplify print in tui view
     */

    /**
     * getter method for attribute receiverNickname
     * @return receiverNickname
     */
    public String getReceiverNickname() {
        return null;
    }
}
