package it.polimi.ingsw.gc07.updates;

import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model_view.GameView;

import java.util.List;

public class CommonObjectiveUpdate implements Update {
    /**
     * Common objective cards.
     */
    private final List<ObjectiveCard> commonObjective;

    /**
     * Constructor of CommonObjectiveUpdate.
     * @param commonObjective common objective cards
     */
    public CommonObjectiveUpdate(List<ObjectiveCard> commonObjective) {
        this.commonObjective = commonObjective;
    }

    /**
     * Execute method of the concrete update: sets common objective cards.
     */
    @Override
    public void execute(GameView gameView) {
        gameView.setCommonObjective(commonObjective);
    }
}
