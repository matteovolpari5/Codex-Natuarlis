package it.polimi.ingsw.gc07.updates;

import it.polimi.ingsw.gc07.model_view.GameView;
import it.polimi.ingsw.gc07.model_view.PlayerView;

import java.util.List;

public class PlayerJoinedUpdate implements Update {
    /**
     * List of player views.
     * It is necessary to send the whole list, because the new player
     * doesn't know previous players.
     */
    private final List<PlayerView> playerViews;

    /**
     * Constructor of PlayerJoinedUpdate.
     * @param playerViews player views
     */
    public PlayerJoinedUpdate(List<PlayerView> playerViews) {
        this.playerViews = playerViews;
    }

    /**
     * Execute method of the concrete update telling a player has joined.
     * @param gameView game view
     */
    @Override
    public void execute(GameView gameView) {
        gameView.setPlayerViews(playerViews);
    }
}
