package it.polimi.ingsw.gc07.updates;

import it.polimi.ingsw.gc07.model_view.GameView;

import java.util.List;

public class GameEndedUpdate implements Update {
    /**
     * List of winners.
     */
    private final List<String> winners;

    /**
     * Constructor of GameEndedUpdate.
     * @param winners list of winners
     */
    public GameEndedUpdate(List<String> winners) {
        this.winners = winners;
    }

    /**
     * Execute method of the concrete update: allows to notify the game winners.
     * @param gameView gameView game view to update
     */
    @Override
    public void execute(GameView gameView) {
        gameView.setWinners(winners);
    }
}
