package it.polimi.ingsw.gc07.view.tui;

import it.polimi.ingsw.gc07.model.chat.ChatMessage;

import java.util.List;

public class ChatTui {
    // TODO come prendo il nickname di chi chiama il metodo?
    public void printChat(List<ChatMessage> chatMessages)
    {
        for(ChatMessage m : chatMessages)
        {
            if(m.getIsPublic())
            {
                System.out.print("["+m.getDateTime()+"] " + "<"+ m.getSender()+"> "+  m.getContent());
            }
            //else if(m.isForReceiver())
        }
    }
}
