package it.polimi.ingsw.gc07.network.ping_receiver;

import it.polimi.ingsw.gc07.network.VirtualView;

import java.util.HashMap;
import java.util.Map;
import java.lang.Thread;

public abstract class PingReceiver {

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
     * Constructor of PingReceiver.
     */
    public PingReceiver() {
        this.playersPing = new HashMap<>();
        this.playerVirtualViews = new HashMap<>();
    }

    /**
     * Method used to add a player to monitor.
     *
     * @param nickname player's virtual nickname
     */
    public synchronized void addPlayer(String nickname) {
        this.playersPing.put(nickname, true);
        new Thread(() -> checkPing(nickname)).start();
    }

    public synchronized void addPingSender(String nickname, VirtualView virtualView) {
        this.playerVirtualViews.put(nickname, virtualView);
    }

    public synchronized VirtualView getVirtualView(String nickname) {
        assert (playerVirtualViews.containsKey(nickname));
        return playerVirtualViews.get(nickname);
    }

    /**
     * Method used to notify the ping receiver that a player disconnected
     * if he asked to do so (via Tui / GUI).
     * It is necessary to set his playersPing to false.
     *
     * @param nickname nickname
     */
    public synchronized void notifyPlayerDisconnected(String nickname) {
        playersPing.put(nickname, false);
    }

    /**
     * Getter for the payers' pings.
     * @return map with players and pings
     */
    protected Map<String, Boolean> getPlayersPing() {
        return playersPing;
    }

    /**
     * Getter for maxPissedPings.
     * @return value of maxMissedPings
     */
    protected int getMaxMissedPings() {return maxMissedPings;}

    /**
     * Getter for the VirtualViews' maps.
     * @return map of the virtualViews
     */
    protected Map<String, VirtualView> getPlayerVirtualViews() {return playerVirtualViews;}
    /**
     * Method that runs for every player and periodically checks if the
     * player sent a ping. After a certain number of pings not sent in a row,
     * detects a disconnection.
     *
     * @param nickname nickname
     */
    public abstract void checkPing(String nickname);
}
