package it.polimi.ingsw.gc07.game_commands;

import it.polimi.ingsw.gc07.controller.GameController;
import it.polimi.ingsw.gc07.network.VirtualView;

public class SendPingCommand implements GameCommand {
    /**
     * Nickname of the player that sends the ping.
     */
    private final String nickname;

    public SendPingCommand(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void execute(GameController gameController) {
        gameController.receivePing(nickname);
    }
}
