package it.polimi.ingsw.gc07.network;

import it.polimi.ingsw.gc07.controller.GameController;
import it.polimi.ingsw.gc07.utils.SafePrinter;

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
        System.out.println("adding player: " + nickname);
        this.playersPing.put(nickname, true);
        this.playerVirtualViews.put(nickname, virtualView);
        new Thread(() -> {checkPing(nickname); System.out.println("Thread checkPong Morto Per: " + nickname);}).start();
        new Thread(() -> {sendPong(nickname); System.out.println("Thread sendPong Morto Per: " + nickname);}).start();
    }

    /**
     * Method used to receive a ping from a player with a certain nickname.
     * @param nickname nickname
     */
    public synchronized void receivePing(String nickname) {
        if(playersPing.containsKey(nickname)) {
            playersPing.put(nickname, true);
        }
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
            if(!gameController.isPlayerConnected(nickname)) {
                break;
            }
            synchronized(this) {
                if(playersPing.get(nickname)) {
                    missedPing = 0;
                }else {
                    missedPing ++;
                    if(missedPing >= maxMissedPings) {
                        SafePrinter.println("CP> Disconnection detected " + nickname);
                        gameController.disconnectPlayer(nickname);
                        //TODO closeConnection
                        break;
                    }
                }
                playersPing.put(nickname, false);
            }
            try {
                Thread.sleep(1000); // wait one second between two pings
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Method used to send a pong update to the player, in order
     * to make it aware connection is stable.
     * @param nickname nickname
     */
    private void sendPong(String nickname) {
        while (true){
            VirtualView virtualView;
            synchronized (this) {
                virtualView = getVirtualView(nickname);
            }
            try {
                System.out.println("sending pong to: " + nickname);
                virtualView.sendPong();
            } catch (RemoteException e) {
                SafePrinter.println("SP> Disconnection detected " + nickname);
                gameController.disconnectPlayer(nickname);
                System.out.println("before break: " + nickname);
                break;
            }
            try {
                Thread.sleep(1000); // wait one second between two pong
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("out of while true: " + nickname);
    }
}

