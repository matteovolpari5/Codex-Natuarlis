package it.polimi.ingsw.gc07.network;

import it.polimi.ingsw.gc07.controller.GameController;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.lang.Thread;

public class PingPongManager {
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
    private static final int maxMissedPings = 3;

    /**
     * Constructor of PingPongManager.
     * @param gameController game controller
     */
    public PingPongManager(GameController gameController) {
        this.gameController = gameController;
        this.playersPing = new HashMap<>();
        this.playerVirtualViews = new HashMap<>();
    }

    public synchronized void addPingSender(String nickname, VirtualView virtualView) {
        this.playersPing.put(nickname, true);
        new Thread(() -> checkPing(nickname)).start();
        new Thread(() -> sendPong(nickname)).start();
        this.playerVirtualViews.put(nickname, virtualView);
    }

    /**
     * Method used to receive a ping from a player with a certain nickname.
     * @param nickname nickname
     */
    public synchronized void receivePing(String nickname) {
        assert (playersPing.containsKey(nickname));
        playersPing.put(nickname, true);
    }

    public synchronized VirtualView getVirtualView(String nickname) {
        assert (playerVirtualViews.containsKey(nickname));
        return playerVirtualViews.get(nickname);
    }

    /**
     * Method used to notify the ping receiver that a player disconnected
     * if he asked to do so (via Tui / GUI).
     * It is necessary to set his playersPing to false.
     * @param nickname nickname
     */
    public synchronized void notifyPlayerDisconnected(String nickname) {
        playersPing.put(nickname, false);
    }

    /**
     * Method that runs for every player and periodically checks if the
     * player sent a ping. After a certain number of pings not sent in a row,
     * detects a disconnection.
     *
     * @param nickname nickname
     */
    public void checkPing(String nickname) {
        int missedPing = 0;
        while(true) {
            synchronized(this) {
                if(playersPing.get(nickname)) {
                    missedPing = 0;
                }else {
                    missedPing ++;
                    System.out.println(missedPing+ nickname);
                    if(missedPing >= maxMissedPings) {
                        System.out.println("PRG> Disconnection detected " + nickname);
                        gameController.disconnectPlayer(nickname);
                        break;
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
    }

    private void sendPong(String nickname) {
        while (true){
            VirtualView virtualView;
            synchronized (this) {
                virtualView = getVirtualView(nickname);
            }
            try {
                virtualView.sendPong();
            } catch (RemoteException e) {
                //TODO rivedere
                System.out.println("PRG> Disconnection detected " + nickname);
                gameController.disconnectPlayer(nickname);
                break;
            }
            try {
                Thread.sleep(1000); // wait one second between two pong
            } catch (InterruptedException e) {
                // TODO
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
}
