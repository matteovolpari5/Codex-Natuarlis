package it.polimi.ingsw.gc07.model;

public class PrivateMessage extends Message {
    private final String receiverNickname;

    public PrivateMessage(String content, String senderNickname, boolean isPublic, String receiverNickname) {
        super(content, senderNickname, isPublic);
        this.receiverNickname = receiverNickname;
    }

    @Override
    public String getReceiver(){
        return receiverNickname;
    }
}
