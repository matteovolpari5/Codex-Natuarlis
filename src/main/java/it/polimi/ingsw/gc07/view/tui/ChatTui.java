package it.polimi.ingsw.gc07.view.tui;

import it.polimi.ingsw.gc07.model.chat.ChatMessage;

import java.util.List;

public interface ChatTui {
    static void printChat(List<ChatMessage> chatMessages) {
        for(ChatMessage m : chatMessages) {
            printMessage(m);
        }
    }

    static void printMessage(ChatMessage m) {
        if(m.getIsPublic())
            System.out.println("["+m.getDateTime().getHours()+":"+m.getDateTime().getMinutes()+"] " + "<"+ m.getSender()+"> :"+  m.getContent());
        else
            System.out.println("["+m.getDateTime().getHours()+":"+m.getDateTime().getMinutes()+"] " + "<"+ m.getSender()+ " to you> :"+  m.getContent());
    }
}
