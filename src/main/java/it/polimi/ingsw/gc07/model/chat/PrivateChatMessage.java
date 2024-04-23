package it.polimi.ingsw.gc07.model.chat;

/**
 * Class representing a private message.
 * PrivateChatMessage is immutable.
 */
public class PrivateChatMessage extends ChatMessage {
    /**
     * String that identifies the receiver by its unique nickname.
     */
    private final String receiverNickname;

    /**
     * Constructor of PrivateChatMessage.
     * @param content content of the message
     * @param senderNickname nickname of the sender
     * @param isPublic boolean value, tells if the message is public (true) or not (false)
     * @param receiverNickname nickname of the receiver
     */
    public PrivateChatMessage(String content, String senderNickname, boolean isPublic, String receiverNickname) {
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
