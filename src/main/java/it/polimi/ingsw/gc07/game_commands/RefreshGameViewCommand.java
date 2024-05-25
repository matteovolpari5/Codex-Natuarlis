package it.polimi.ingsw.gc07.game_commands;

import it.polimi.ingsw.gc07.controller.GameController;

public class RefreshGameViewCommand implements GameControllerCommand {
    /**
     * Player's nickname.
     */
    private final String nickname;

    /**
     * Constructor of RefreshGameViewCommand.
     * @param nickname nickname
     */
    public RefreshGameViewCommand(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Method used to execute the game command.
     * @param gameController game controller
     */
    @Override
    public void execute(GameController gameController) {
        gameController.sendModelView(nickname);
    }
}
