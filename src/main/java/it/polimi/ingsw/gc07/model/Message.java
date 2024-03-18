package it.polimi.ingsw.gc07.model;

public class Message {
    /**
     * String containing the body of the message.
     */
    private final String content;
    /**
     * String that identifies the sender, by its unique nickname.
     */
    private final String senderNickname;
    /**
     * Boolean attribute, that tells if the message is public.
     * If true, the message is public, i.e. visible by all the players in the same game.
     */
    private final boolean isPublic;

    /**
     * Constructor for Message.
     * @param content Content of the message
     * @param senderNickname Nickname of the sender
     * @param isPublic Boolean value, tells if the message is public (true) or not (false)
     */
    public Message(String content, String senderNickname, boolean isPublic) {
        this.content = content;
        this.senderNickname = senderNickname;
        this.isPublic = isPublic;
    }

    public String getContent(){
        return content;
    }

    public String getSender(){
        return senderNickname;
    }

    public boolean isPublic(){
        return isPublic;
    }

    public String getReceiver(){
        return null;
    }
}
