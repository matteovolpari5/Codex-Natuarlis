package it.polimi.ingsw.gc07.updates;

import it.polimi.ingsw.gc07.model.enumerations.CommandResult;
import it.polimi.ingsw.gc07.model_view.GameView;

public class CommandResultUpdate implements Update {
    /**
     * Nickname of the player who performed an action.
     */
    private final String nickname;
    /**
     * Command result.
     */
    private final CommandResult commandResult;

    /**
     * Constructor of CommandResultUpdate.
     * @param commandResult command result
     */
    public CommandResultUpdate(String nickname, CommandResult commandResult) {
        this.nickname = nickname;
        this.commandResult = commandResult;
    }

    /**
     * Execute method of the concrete update: allows to notify a command result.
     * @param gameView gameView game view to update
     */
    @Override
    public void execute(GameView gameView) {
        gameView.setCommandResult(nickname, commandResult);
    }
}
