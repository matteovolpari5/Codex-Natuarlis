package it.polimi.ingsw.gc07.network;

import it.polimi.ingsw.gc07.controller.GameController;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.network.rmi.RmiClient;
import it.polimi.ingsw.gc07.network.rmi.RmiServerGame;
import it.polimi.ingsw.gc07.network.rmi.RmiServerGamesManager;

import java.rmi.RemoteException;
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

    public synchronized void receivePing(String nickname) {
        assert(playersPing.containsKey(nickname));
        playersPing.put(nickname, true);
        System.out.println("ping inviato");
    }

    private void checkPing(String nickname) {
        int missedPing = 0;
        while(true) {

            synchronized(this) {
                if(playersPing.get(nickname)) {
                    missedPing = 0;
                    if(!gameController.isPlayerConnected(nickname)) {
                        Player player = gameController.getPlayerByNickname(nickname);
                        assert (player != null);
                        if (player.getConnectionType()) {
                            // RMI
                            try {
                                RmiClient client = new RmiClient(nickname, RmiServerGamesManager.getRmiServerGamesManager());
                                gameController.reconnectPlayer(nickname, client);
                            } catch (RemoteException e) {
                                // TODO
                                e.printStackTrace();
                                throw new RuntimeException(e);
                            }
                        } else {
                            // TODO socket
                        }
                    }
                }else {
                    missedPing ++;
                    if(missedPing >= maxMissedPings && gameController.isPlayerConnected(nickname)) {
                        gameController.disconnectPlayer(nickname); // TODO metodo deve synchronized
                    }
                }
                playersPing.put(nickname, false);
            }
            try {
                Thread.sleep(1000); // wait one second between two ping
            } catch (InterruptedException e) {
                // TODO
                e.printStackTrace();
                throw new RuntimeException(e);
            }

        }


        // TODO invio pong
    }
}
