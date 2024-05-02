package it.polimi.ingsw.gc07.network;

import it.polimi.ingsw.gc07.controller.GameController;
import it.polimi.ingsw.gc07.game_commands.SendPingCommand;

public class ClientPingSender {

    public ClientPingSender(GameController game){
    }

    public void startCommunication(String nickname){
        new Thread(()->{
            while (true){
                .setAndExecuteCommand(new SendPingCommand(nickname));
                try {
                    Thread.sleep(1000); // wait one second between two ping
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                //todo ricezione del pong
            }
        }).start();
    }
}