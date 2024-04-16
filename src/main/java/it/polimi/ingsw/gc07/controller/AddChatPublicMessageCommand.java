package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.controller.enumerations.CommandResult;
import it.polimi.ingsw.gc07.model.Player;

import java.util.List;

/**
 * Concrete command to add a public message to the chat.
 */
public class AddChatPublicMessageCommand extends GameCommand {
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
     * @param content content of the message
     * @param sender sender of the message
     */
    public AddChatPublicMessageCommand(String content, String sender) {
        this.content = content;
        this.sender = sender;
    }

    /**
     * Method to execute the concrete command AddChatPublicMessageCommand.
     */
    @Override
    public void execute(Game game) {
        // no state check, this command be used all the time
        List<String> playersNicknames = game.getPlayers().stream().map(Player::getNickname).toList();
        // check valid sender
        if(!playersNicknames.contains(sender)){
            game.getCommandResultManager().setCommandResult(CommandResult.WRONG_SENDER);
            return;
        }
        // add message to chat
        game.getChat().addPublicMessage(content, sender, playersNicknames);
        game.getCommandResultManager().setCommandResult(CommandResult.SUCCESS);
    }
}
