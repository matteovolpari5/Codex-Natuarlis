package it.polimi.ingsw.gc07.updates;

import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model_view.GameView;

import java.util.List;

public class DeckUpdate implements Update {
    private final DrawableCard topResourceCard;
    private final GoldCard topGoldCard;
    private final List<DrawableCard> resourceFaceUpCards;
    private final List<GoldCard> goldFaceUpCards;

    public DeckUpdate(DrawableCard topResourceCard, GoldCard topGoldCard, List<DrawableCard> resourceFaceUpCards, List<GoldCard> goldFaceUpCards) {
        this.topResourceCard = topResourceCard;
        this.topGoldCard = topGoldCard;
        this.resourceFaceUpCards = resourceFaceUpCards;
        this.goldFaceUpCards = goldFaceUpCards;
    }

    @Override
    public void execute(GameView gameView) {
        gameView.setTopResourceCard(topResourceCard);
        gameView.setTopGoldCard(topGoldCard);
        gameView.setResourceFaceUpCards(resourceFaceUpCards);
        gameView.setGoldFaceUpCards(goldFaceUpCards);
    }
}
