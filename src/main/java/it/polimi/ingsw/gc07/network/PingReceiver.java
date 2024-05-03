package it.polimi.ingsw.gc07.network;

import it.polimi.ingsw.gc07.controller.GameController;

import java.util.HashMap;
import java.util.Map;
import java.lang.Thread;

public class PingReceiver {
    private final GameController gameController;
    private final Map<String, Boolean> playersPing;
    private static final int maxMissedPings = 2;

    public PingReceiver(GameController gameController) {
        this.gameController = gameController;
        this.playersPing = new HashMap<>();
    }

    public void addPlayer(String nickname) {
        this.playersPing.put(nickname, true);
        new Thread(() -> checkPing(nickname)).start();
    }

    public void receivePing(String nickname) {
        assert(playersPing.containsKey(nickname));
        playersPing.put(nickname, true);
    }

    private void checkPing(String nickname) {
        /*
        int missedPing = 0;
        while(true) {
            if(playersPing.get(nickname)) {
                missedPing = 0;
                if(!gameController.isPlayerConnected(nickname)) {
                    gameController.reconnectPlayer(nickname);
                }
            }else {
                missedPing ++;
                if(missedPing >= maxMissedPings && gameController.isPlayerConnected(nickname)) {
                    gameController.disconnectPlayer(nickname); // TODO metodo deve synchronized
                }
            }
            playersPing.put(nickname, false);
            try {
                Thread.sleep(2000); // wait one second between two ping
            } catch (InterruptedException e) {
                // TODO
                e.printStackTrace();
                throw new RuntimeException(e);
            }




         */
            // TODO invio pong
    }
}
