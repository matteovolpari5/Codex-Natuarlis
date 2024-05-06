package it.polimi.ingsw.gc07.view.tui;

import it.polimi.ingsw.gc07.model.chat.ChatMessage;
import it.polimi.ingsw.gc07.model_view_listeners.ChatViewListener;

import java.util.List;

public class ChatTui implements ChatViewListener {

    private void printChat(List<ChatMessage> chatMessages) {
        for(ChatMessage m : chatMessages) {
            printMessage(m);
        }
    }

    private void printMessage(ChatMessage m) {
        if(m.getIsPublic())
            System.out.println("["+m.getDateTime().getHours()+":"+m.getDateTime().getMinutes()+"] " + "<"+ m.getSender()+"> :"+  m.getContent());
        else
            System.out.println("["+m.getDateTime().getHours()+":"+m.getDateTime().getMinutes()+"] " + "<"+ m.getSender()+ " to you> :"+  m.getContent());

    }

    @Override
    public void receiveMessageUpdate(ChatMessage chatMessage) {
        printMessage(chatMessage);
    }
}
