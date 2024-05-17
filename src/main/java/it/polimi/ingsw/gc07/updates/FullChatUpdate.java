package it.polimi.ingsw.gc07.updates;

import it.polimi.ingsw.gc07.model.chat.ChatMessage;
import it.polimi.ingsw.gc07.model_view.GameView;

import java.util.List;

public class FullChatUpdate implements Update {
    private final List<ChatMessage> chatMessages;

    public FullChatUpdate(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @Override
    public void execute(GameView gameView) {
        gameView.setChatMessages(chatMessages);
    }
}
