package it.polimi.ingsw.gc07.model.chat;

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

    /**
     * Getter for the Message's content.
     * @return content of the message
     */
    public String getContent(){
        return content;
    }

    /**
     * Getter for the Message's sender.
     * @return nickname of the Message's sender
     */
    public String getSender(){
        return senderNickname;
    }

    /**
     * Getter for the isPublic attribute
     * @return a boolean representing the isPublic attribute
     */
    public boolean isPublic(){
        return isPublic;
    }

    /**
     * Getter for the Message's receiver.
     * @return nickname of the Message's receiver
     */
    public String getReceiver(){
        return null;
    }

    /**
     * Method that checks if the message is for a certain receiver.
     * @param receiver nickname of the receiver
     * @return true if the message is for the receiver, false in other cases
     */
    public boolean isForReceiver(String receiver){
        return true;
    }
}
