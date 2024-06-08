package it.polimi.ingsw.gc07.view.tui;

import it.polimi.ingsw.gc07.model.chat.ChatMessage;
import it.polimi.ingsw.gc07.utils.SafePrinter;

import java.util.List;

/**
 * Interface containing methods to print Chat.
 */
public interface ChatTui {
    /**
     * Method used to print the whole chat.
     * @param chatMessages chat messages
     */
    static void printChat(List<ChatMessage> chatMessages) {
        for(ChatMessage m : chatMessages) {
            printMessage(m);
        }
    }

    /**
     * Method used to print a single chat message.
     * @param m chat message
     */
    static void printMessage(ChatMessage m) {
        if(m.getIsPublic()) {
            SafePrinter.println("[" + m.getDateTime().getHours() + ":" + m.getDateTime().getMinutes() + "] " + "<" + m.getSender() + " to all> : " + m.getContent());
        }else {
            SafePrinter.println("[" + m.getDateTime().getHours() + ":" + m.getDateTime().getMinutes() + "] " + "<" + m.getSender() + " to you> : " + m.getContent());
        }
    }
}
