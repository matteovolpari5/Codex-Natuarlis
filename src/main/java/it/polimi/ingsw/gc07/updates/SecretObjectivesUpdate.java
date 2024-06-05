package it.polimi.ingsw.gc07.updates;

import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model_view.GameView;

import java.util.List;

/**
 * Update used to notify a player of his secret objectives cards.
 */
public class SecretObjectivesUpdate implements Update {
    /**
     * Player's nickname.
     */
    private final String nickname;
    /**
     * Personal objectives.
     */
    private final List<ObjectiveCard> objectiveCards;

    /**
     * Constructor of SecretObjectivesUpdate.
     * @param nickname nickname
     * @param objectiveCards objective cards
     */
    public SecretObjectivesUpdate(String nickname, List<ObjectiveCard> objectiveCards) {
        this.nickname = nickname;
        this.objectiveCards = objectiveCards;
    }

    /**
     * Method that allows the client to get his personal objectives.
     * @param gameView game view
     */
    @Override
    public void execute(GameView gameView) {
        gameView.setSecretObjectives(nickname, objectiveCards);
    }
}
