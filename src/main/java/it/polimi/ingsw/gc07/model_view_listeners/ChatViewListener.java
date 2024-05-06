package it.polimi.ingsw.gc07.model_view_listeners;

import it.polimi.ingsw.gc07.model.chat.ChatMessage;

public interface ChatViewListener {
    void receiveMessageUpdate(ChatMessage chatMessage);
}
