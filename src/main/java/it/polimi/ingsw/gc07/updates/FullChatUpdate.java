package it.polimi.ingsw.gc07.updates;

import it.polimi.ingsw.gc07.model.chat.ChatMessage;
import it.polimi.ingsw.gc07.model_view.GameView;

import java.util.List;

public class FullChatUpdate implements Update {
    /**
     * Full chat content.
     */
    private final List<ChatMessage> chatMessages;

    /**
     * Constructor of FullChatUpdate.
     * @param chatMessages chat messages
     */
    public FullChatUpdate(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    /**
     * Execute method of the concrete update: full chat update.
     * @param gameView game view
     */
    @Override
    public void execute(GameView gameView) {
        gameView.setChatMessages(chatMessages);
    }
}
