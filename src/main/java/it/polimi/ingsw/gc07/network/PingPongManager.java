package it.polimi.ingsw.gc07.network;

import it.polimi.ingsw.gc07.controller.GameController;
import it.polimi.ingsw.gc07.utils.SafePrinter;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.lang.Thread;

/**
 * Class used by the game controller to manage connections and disconnections in the game.
 */
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

    /**
     * Method used to add a ping sender to the PingPongManager.
     * @param nickname ping sender's nickname
     * @param virtualView ping sender's virtual view
     */
    public synchronized void addPingSender(String nickname, VirtualView virtualView) {
        System.out.println("Adding player: " + nickname);
        this.playersPing.put(nickname, true);
        this.playerVirtualViews.put(nickname, virtualView);
        new Thread(() -> {checkPing(nickname); SafePrinter.println("Thread checkPing Morto Per: " + nickname);}).start();
        new Thread(() -> {sendPong(nickname, virtualView); SafePrinter.println("Thread sendPong Morto Per: " + nickname);}).start();
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

    /**
     * Getter method for the virtual view associated to the nickname.
     * @param nickname player's nickname
     * @return player's virtual view
     */
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
     * @param nickname nickname
     */
    public void checkPing(String nickname) {
        int missedPing = 0;
        while(true) {
            // check player connected
            if(!gameController.isPlayerConnected(nickname)) {
                break;
            }
            synchronized(this) {
                // check and clean received ping value
                if(playersPing.get(nickname)) {
                    missedPing = 0;
                }else {
                    missedPing ++;
                    System.out.println("missed ping for " + nickname);
                    if(missedPing >= maxMissedPings) {
                        System.out.println("disconnecting " + nickname + " for check ping");
                        // disconnect player
                        gameController.disconnectPlayer(nickname);

                        // close connection with socket client
                        if(!gameController.getPlayerConnection(nickname)) {
                            VirtualView virtualView = getVirtualView(nickname);
                            try {
                                virtualView.closeConnection();
                            } catch (RemoteException e) {
                                // it is not necessary to manage the RMI exception
                            }
                        }
                        break;
                    }
                }
                playersPing.put(nickname, false);
            }
            try {
                Thread.sleep(1000);
                // wait one second between two pings
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Method used to send a pong update to the player, in order
     * to make it aware connection is stable.
     * @param nickname client's nickname
     * @param virtualView client's virtualView
     */
    private void sendPong(String nickname, VirtualView virtualView) {
        while (true){
            // TODO send pong si blocca qua, se il controller è bloccato,
            // il client non riceve ping e cade la connessione
            if(!gameController.isPlayerConnected(nickname)) {
                break;
            }

            try {
                virtualView.sendPong();
            }catch(RemoteException e) {
                if(virtualView.equals(getVirtualView(nickname))) {
                    System.out.println("disconnecting " + nickname + " for send pong");
                    // if the client has not reconnected
                    gameController.disconnectPlayer(nickname);
                }
                break;
            }

            try {
                Thread.sleep(1000);
                // wait one second between two pongs
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

