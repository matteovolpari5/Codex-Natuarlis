package it.polimi.ingsw.gc07.controller;

public class NotifyClientConnected extends GameCommand{
    String nickname;
    public NotifyClientConnected(String nickname){
        this.nickname = nickname;
    }
    @Override
    public void execute(Game game) {
        game.notifyClientConnected(nickname);
    }
}
