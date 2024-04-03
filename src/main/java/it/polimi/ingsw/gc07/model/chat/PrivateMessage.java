package it.polimi.ingsw.gc07.model.chat;

/**
 * Class representing a private message.
 * PrivateMessage is immutable.
 */
public class PrivateMessage extends Message {
    /**
     * String that identifies the receiver by its unique nickname.
     */
    private final String receiverNickname;

    /**
     * Constructor of PrivateMessage.
     * @param content content of the message
     * @param senderNickname nickname of the sender
     * @param isPublic boolean value, tells if the message is public (true) or not (false)
     * @param receiverNickname nickname of the receiver
     */
    public PrivateMessage(String content, String senderNickname, boolean isPublic, String receiverNickname) {
        super(content, senderNickname, isPublic);
        this.receiverNickname = receiverNickname;
    }

    /**
     * Method that checks if the message is for a certain receiver.
     * @param receiver nickname of the receiver
     * @return true if the message is for the receiver, false in other cases
     */
    @Override
    public boolean isForReceiver(String receiver){
        return receiverNickname.equals(receiver);
    }
}
