package it.polimi.ingsw.gc07.game_commands;

import it.polimi.ingsw.gc07.controller.GameController;

public class NotifyClientConnected extends GameCommand {
    String nickname;
    public NotifyClientConnected(String nickname){
        this.nickname = nickname;
    }
    @Override
    public void execute(GameController gameController) {
        gameController.notifyClientConnected(nickname);
    }
}
