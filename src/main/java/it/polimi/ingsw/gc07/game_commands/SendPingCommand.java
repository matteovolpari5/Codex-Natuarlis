package it.polimi.ingsw.gc07.game_commands;

import it.polimi.ingsw.gc07.controller.GameController;
import it.polimi.ingsw.gc07.network.VirtualView;

public class SendPingCommand implements GameCommand {
    /**
     * Nickname of the player that sends the ping.
     */
    private final String nickname;
    /**
     * Virtual view.
     */
    private final VirtualView virtualView;

    public SendPingCommand(String nickname, VirtualView virtualView) {
        this.nickname = nickname;
        this.virtualView = virtualView;
    }

    @Override
    public void execute(GameController gameController) {
        gameController.receivePing(nickname, virtualView);
    }
}
