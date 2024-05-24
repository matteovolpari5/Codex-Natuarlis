package it.polimi.ingsw.gc07.game_commands;

import it.polimi.ingsw.gc07.controller.GameController;

public class SendPingCommand implements GameControllerCommand {
    /**
     * Nickname of the player that sends the ping.
     */
    private final String nickname;

    /**
     * Constructor of SendPingCommand.
     * @param nickname nickname
     */
    public SendPingCommand(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Method used to send a ping to the server.
     * @param gameController game controller
     */
    @Override
    public void execute(GameController gameController) {
        gameController.receivePing(nickname);
    }
}
