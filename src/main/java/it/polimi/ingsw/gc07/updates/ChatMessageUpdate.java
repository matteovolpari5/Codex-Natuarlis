package it.polimi.ingsw.gc07.updates;

import it.polimi.ingsw.gc07.model.chat.ChatMessage;
import it.polimi.ingsw.gc07.model_view.GameView;

public class ChatMessageUpdate implements Update {
    /**
     * New ChatMessage.
     */
    private final ChatMessage chatMessage;

    /**
     * Constructor of ChatMessageUpdate.
     * @param chatMessage new ChatMessage
     */
    public ChatMessageUpdate(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    /**
     * Execute method of the concrete update: adds the message to ChatView.
     * @param gameView game view to update
     */
    @Override
    public void execute(GameView gameView) {
        gameView.addMessage(chatMessage);
    }
}
