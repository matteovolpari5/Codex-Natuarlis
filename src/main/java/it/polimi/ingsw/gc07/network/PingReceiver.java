package it.polimi.ingsw.gc07.network;

import it.polimi.ingsw.gc07.controller.GameController;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.network.rmi.RmiClient;
import it.polimi.ingsw.gc07.network.rmi.RmiServerGamesManager;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.lang.Thread;

public class PingReceiver {
    /**
     * Game Controller.
     */
    private final GameController gameController;
    /**
     * Map containing all player in the game and a corresponding boolean value,
     * which tells if they sent the last ping or not.
     */
    private final Map<String, Boolean> playersPing;
    /**
     * Map containing all players in the game with their virtual view,
     * if a disconnection occurs, I need to know their virtual view.
     */
    private final Map<String, VirtualView> playerVirtualViews;
    /**
     * Number of missed pings to detect a disconnection.
     */
    private static final int maxMissedPings = 2;

    /**
     * Constructor of PingReceiver.
     * @param gameController game controller
     */
    public PingReceiver(GameController gameController) {
        this.gameController = gameController;
        this.playersPing = new HashMap<>();
        this.playerVirtualViews = new HashMap<>();
    }

    /**
     * Method used to add a player to monitor.
     * @param nickname player's virtual nickname
     */
    public synchronized void addPlayer(String nickname) {
        this.playersPing.put(nickname, true);
        new Thread(() -> checkPing(nickname)).start();
    }

    public synchronized void setVirtualView(String nickname, VirtualView virtualView) {
        this.playerVirtualViews.put(nickname, virtualView);
    }

    /**
     * Method used to notify the ping receiver that a player disconnected
     * if he asked to do so (via TUI / GUI).
     * It is necessary to set his playersPing to false.
     * @param nickname nickname
     */
    public synchronized void notifyPlayerDisconnected(String nickname) {
        playersPing.put(nickname, false);
    }

    /**
     * Method used to receive a ping from a player with a certain nickname.
     * @param nickname nickname
     */
    public synchronized void receivePing(String nickname) {
        assert(playersPing.containsKey(nickname));
        playersPing.put(nickname, true);
        System.out.println("ping inviato " +  nickname);
    }

    /**
     * Method that runs for every player and periodically checks if the
     * player sent a ping. After a certain number of pings not sent in a row,
     * detects a disconnection.
     * @param nickname nickname
     */
    private void checkPing(String nickname) {
        int missedPing = 0;
        while(true) {
            synchronized(this) {
                if(playersPing.get(nickname)) {
                    missedPing = 0;
                    if(!gameController.isPlayerConnected(nickname)) {
                        System.out.println("Reconnection detected " + nickname);
                        Player player = gameController.getPlayerByNickname(nickname);
                        assert (player != null);
                        if (player.getConnectionType()) {
                            // RMI
                            try {
                                RmiClient client = new RmiClient(nickname, RmiServerGamesManager.getRmiServerGamesManager());
                                gameController.reconnectPlayer(client, nickname);
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
                        System.out.println("Disconnection detected " + nickname);
                        gameController.disconnectPlayer(nickname, playerVirtualViews.get(nickname)); // TODO metodo deve synchronized
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
