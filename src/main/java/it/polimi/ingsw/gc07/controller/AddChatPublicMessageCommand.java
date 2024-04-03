package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.controller.enumerations.CommandResult;
import it.polimi.ingsw.gc07.model.Player;

import java.util.List;

/**
 * Concrete command to add a public message to the chat.
 */
public class AddChatPublicMessageCommand implements GameCommand {
    /**
     * Game in which the command has to be executed.
     */
    private final Game game;
    /**
     * Content of the public message.
     */
    private final String content;
    /**
     * Sender of the public message.
     */
    private final String sender;

    /**
     * Constructor of the concrete command AddChatPublicMessageCommand.
     * @param game game in which the command has to be executed
     * @param content content of the message
     * @param sender sender of the message
     */
    public AddChatPublicMessageCommand(Game game, String content, String sender) {
        this.game = game;
        this.content = content;
        this.sender = sender;
    }

    /**
     * Method to execute the concrete command AddChatPublicMessageCommand.
     * @return command result
     */
    @Override
    public CommandResult execute() {
        List<String> playersNicknames = game.getPlayers().stream().map(Player::getNickname).toList();
        // check valid sender
        if(!playersNicknames.contains(sender))
            return CommandResult.WRONG_SENDER;
        // add message to chat
        game.getChat().addPublicMessage(content, sender, playersNicknames);
        return CommandResult.SUCCESS;
    }
}
