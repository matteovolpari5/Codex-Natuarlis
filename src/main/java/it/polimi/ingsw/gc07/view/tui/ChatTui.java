package it.polimi.ingsw.gc07.view.tui;

import it.polimi.ingsw.gc07.model.chat.ChatMessage;

import java.util.List;

public class ChatTui {

    public void printChat(List<ChatMessage> chatMessages)
    {
        for(ChatMessage m : chatMessages)
        {
            if(m.getIsPublic())
                System.out.println("["+m.getDateTime()+"] " + "<"+ m.getSender()+"> :"+  m.getContent());
            else
                System.out.println("["+m.getDateTime()+"] " + "<"+ m.getSender()+ " to " +m.getReceiverNickname()+" > :"+  m.getContent());
        }
    }
}
