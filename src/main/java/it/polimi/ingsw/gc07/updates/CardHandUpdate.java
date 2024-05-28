package it.polimi.ingsw.gc07.updates;

import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model_view.GameView;

import java.util.List;

public class CardHandUpdate implements Update {
    /**
     * Player's nickname.
     */
    private final String nickname;
    /**
     * New card hand.
     */
    private final List<DrawableCard> cardHand;
    /**
     * Personal objective card.
     */
    private final List<ObjectiveCard> personalObjectives;

    /**
     * Constructor of CardHandUpdate.
     * @param nickname nickname
     * @param cardHand card hand
     */
    public CardHandUpdate(String nickname, List<DrawableCard> cardHand, List<ObjectiveCard> personalObjectives) {
        this.nickname = nickname;
        this.cardHand = cardHand;
        this.personalObjectives = personalObjectives;
    }

    /**
     * Execute method of the concrete update: notifies the card hand has changed.
     * @param gameView game view to update
     */
    @Override
    public void execute(GameView gameView) {
        gameView.setCardHand(nickname, cardHand, personalObjectives);
    }
}
