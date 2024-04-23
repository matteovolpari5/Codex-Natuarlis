package it.polimi.ingsw.gc07.messages;

public interface Message {
    // command pattern
    /**
     * Method that allows the client to get the update in the message.
     */
    void showUpdate();

    // invio un Message, la VirtualView sa che deve farci solo .showUpdate
    // come???
}
