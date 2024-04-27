package it.polimi.ingsw.gc07.updates;

import it.polimi.ingsw.gc07.model_view.GameView;
import it.polimi.ingsw.gc07.model_view.PlayerView;

public class PlayerJoinedUpdate implements Update {
    /**
     * New PlayerView.
     */
    private final PlayerView playerView;

    /**
     * Constructor of PlayerJoinedUpdate.
     * @param playerView playerView
     */
    public PlayerJoinedUpdate(PlayerView playerView) {
        this.playerView = playerView;
    }

    /**
     * Execute method of the concrete update: allows to notify that a player has joined.
     * @param gameView gameView game view to update
     */
    @Override
    public void execute(GameView gameView) {
        gameView.addPlayerView(playerView);
    }
}
