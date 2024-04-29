package it.polimi.ingsw.gc07.updates;

import it.polimi.ingsw.gc07.model_view.GameView;

import java.util.Map;

public class ExistingGamesUpdate implements Update {
    /**
     * Map containing existing games.
     * Key = game id.
     * Value = number of players.
     */
    private final Map<Integer, Integer> existingGames;

    /**
     * Constructor of ExistingGamesUpdate.
     * @param existingGames existing games map
     */
    public ExistingGamesUpdate(Map<Integer, Integer> existingGames) {
        this.existingGames = existingGames;
    }

    /**
     * Execute method of the concrete update: reveal existing games.
     * @param gameView game view
     */
    @Override
    public void execute(GameView gameView) {
        gameView.displayExistingGames(existingGames);
    }
}
