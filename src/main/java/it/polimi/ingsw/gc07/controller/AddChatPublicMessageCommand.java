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
     * This constructor takes parameter game, used by the server.
     * @param game game in which the command has to be executed
     * @param content content of the message
     * @param sender sender of the message
     */
    public AddChatPublicMessageCommand(Game game, String content, String sender) {
        setGame(game);
        this.content = content;
        this.sender = sender;
    }

    /**
     * Constructor of the concrete command AddChatPublicMessageCommand.
     * This constructor doesn't take a game as parameter, used by the client.
     * @param content content of the message
     * @param sender sender of the message
     */
    public AddChatPublicMessageCommand(String content, String sender) {
        setGame(null);
        this.content = content;
        this.sender = sender;
    }

    /**
     * Method to execute the concrete command AddChatPublicMessageCommand.
     */
    @Override
    public void execute() {
        Game game = getGame();

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
