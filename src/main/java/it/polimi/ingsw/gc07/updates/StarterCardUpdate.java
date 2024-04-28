package it.polimi.ingsw.gc07.updates;

import it.polimi.ingsw.gc07.model.cards.PlaceableCard;
import it.polimi.ingsw.gc07.model_view.GameView;

public class StarterCardUpdate implements Update {
    /**
     * Player's nickname.
     */
    private final String nickname;
    /**
     * Starter card.
     */
    private final PlaceableCard starterCard;

    /**
     * Constructor of StarterCardUpdate.
     * @param starterCard starter card to show
     */
    public StarterCardUpdate(String nickname, PlaceableCard starterCard) {
        this.nickname = nickname;
        this.starterCard = starterCard;
    }

    /**
     * Execute method of the concrete update: reveals the starter card to the player.
     * @param gameView game view to update
     */
    @Override
    public void execute(GameView gameView) {
        gameView.setStarterCard(nickname, starterCard);
    }
}
