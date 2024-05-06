package it.polimi.ingsw.gc07.game_commands;

import it.polimi.ingsw.gc07.controller.GameController;

/**
 * Concrete command to add a public message to the chat.
 */
public class AddChatPublicMessageControllerCommand implements GameControllerCommand {
    /**
     * Content of the public message.
     */
    private final String content;
    /**
     * Sender of the public message.
     */
    private final String sender;

    /**
     * Constructor of the concrete command AddChatPublicMessageControllerCommand.
     * @param content content of the message
     * @param sender sender of the message
     */
    public AddChatPublicMessageControllerCommand(String content, String sender) {
        this.content = content;
        this.sender = sender;
    }

    /**
     * Method to execute the concrete command AddChatPublicMessageControllerCommand.
     */
    @Override
    public void execute(GameController gameController) {
        gameController.addChatPublicMessage(content, sender);
    }
}
