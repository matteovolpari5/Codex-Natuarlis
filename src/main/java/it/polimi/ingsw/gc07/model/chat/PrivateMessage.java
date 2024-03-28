package it.polimi.ingsw.gc07.model.chat;

public class PrivateMessage extends Message {
    private final String receiverNickname;

    public PrivateMessage(String content, String senderNickname, boolean isPublic, String receiverNickname) {
        super(content, senderNickname, isPublic);
        this.receiverNickname = receiverNickname;
    }

    /**
     * Getter for the Message's receiver.
     * @return nickname of the Message's receiver
     */
    @Override
    public String getReceiver(){
        return receiverNickname;
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
