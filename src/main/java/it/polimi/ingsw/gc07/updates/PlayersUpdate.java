package it.polimi.ingsw.gc07.updates;

import it.polimi.ingsw.gc07.model_view.GameView;
import it.polimi.ingsw.gc07.model_view.PlayerView;

import java.util.List;

/**
 * Update used to show the list of players in the game has changed: used when players join.
 */
public class PlayersUpdate implements Update {
    /**
     * List of player views.
     * It is necessary to send the whole list, because the new player
     * doesn't know previous players.
     */
    private final List<PlayerView> playerViews;

    /**
     * Constructor of PlayersUpdate.
     * @param playerViews player views
     */
    public PlayersUpdate(List<PlayerView> playerViews) {
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
