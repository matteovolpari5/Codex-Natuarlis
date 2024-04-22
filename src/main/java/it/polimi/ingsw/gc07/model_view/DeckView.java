package it.polimi.ingsw.gc07.model_view;

import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;

import java.util.List;

public class DeckView {
    /**
     * attribute that represent the common objective
     */
    private final List<ObjectiveCard> commonObjective;
    /**
     * attribute that represent the revealed gold cards
     */
    private List<GoldCard> faceUpGoldCard;
    /**
     * attribute that represent the revealed resource cards
     */
    private List<DrawableCard> faceUpResourceCard;
    /**
     * attribute that represent the card on top of the gold deck
     */
    private GoldCard topGoldDeck;
    /**
     * attribute that represent the card on top of the reosurce deck
     */
    private DrawableCard topResourceDeck;

    /**
     * Constructor method for DeckView.
     */
    public DeckView(List<GoldCard> faceUpGoldCard, List<ObjectiveCard> commonObjective, List<DrawableCard> faceUpResourceCard, GoldCard topGoldDeck, DrawableCard topResourceDeck) {
        this.faceUpGoldCard = faceUpGoldCard;
        this.commonObjective = commonObjective;
        this.faceUpResourceCard = faceUpResourceCard;
        this.topGoldDeck = topGoldDeck;
        this.topResourceDeck = topResourceDeck;
    }

    /**
     * setter for attribute faceUpGoldCard
     * @param faceUpGoldCard new face up cards
     */
    public void setFaceUpGoldCard(List<GoldCard> faceUpGoldCard) {
        this.faceUpGoldCard = faceUpGoldCard;
    }

    /**
     * setter for attribute faceUpResourceCard
     * @param faceUpResourceCard new face up cards
     */
    public void setFaceUpResourceCard(List<DrawableCard> faceUpResourceCard) {
        this.faceUpResourceCard = faceUpResourceCard;
    }


    /**
     * setter for attribute topGoldDeck
     * @param topGoldDeck new top card of the gold deck
     */
    public void setTopGoldDeck(GoldCard topGoldDeck) {
        this.topGoldDeck = topGoldDeck;
    }

    /**
     * setter for attribute topResourceDeck
     * @param topResourceDeck new top card of the resource deck
     */
    public void setTopResourceDeck(DrawableCard topResourceDeck) {
        this.topResourceDeck = topResourceDeck;
    }
}

