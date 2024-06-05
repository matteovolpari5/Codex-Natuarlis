package it.polimi.ingsw.gc07.updates;

import it.polimi.ingsw.gc07.model_view.GameView;

/**
 * Update used to show the new connection status of a player in the same game.
 */
public class ConnectionUpdate implements Update {
    /**
     * Player's nickname.
     */
    private final String nickname;
    /**
     * Player's connection status.
     */
    private final boolean isConnected;

    /**
     * Constructor of ConnectionUpdate.
     * @param nickname nickname
     * @param isConnected connection status
     */
    public ConnectionUpdate(String nickname, boolean isConnected) {
        this.nickname = nickname;
        this.isConnected = isConnected;
    }

    /**
     * Execute method of the concrete update: allows to notify a player's connection status.
     * @param gameView game view to update
     */
    @Override
    public void execute(GameView gameView) {
        gameView.setIsConnected(nickname, isConnected);
    }
}
