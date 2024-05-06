package it.polimi.ingsw.gc07.game_commands;

import it.polimi.ingsw.gc07.controller.GameController;

/**
 * Concrete command to add a private message to the chat.
 */
public class AddChatPrivateMessageControllerCommand implements GameControllerCommand {
    /**
     * Content of the private message.
     */
    private final String content;
    /**
     * Sender of the private message.
     */
    private final String sender;
    /**
     * Receiver of the private message.
     */
    private final String receiver;

    /**
     * Constructor of the concrete command AddChatMessageCommand.
     * @param content content
     * @param sender sender nickname
     * @param receiver receiver nickname
     */
    public AddChatPrivateMessageControllerCommand(String content, String sender, String receiver) {
        this.content = content;
        this.sender = sender;
        this.receiver = receiver;
    }

    /**
     * Method to execute the concrete command AddChatPrivateMessageControllerCommand.
     */
    @Override
    public void execute(GameController gameController) {
        gameController.addChatPrivateMessage(content, sender, receiver);
    }
}
