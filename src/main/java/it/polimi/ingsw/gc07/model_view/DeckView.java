package it.polimi.ingsw.gc07.model_view;

import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;

import java.util.List;

public class DeckView {
    /**
     * Attribute that represents common objective cards.
     */
    private List<ObjectiveCard> commonObjective;
    /**
     * Attribute that represents the revealed gold cards.
     */
    private List<GoldCard> faceUpGoldCard;
    /**
     * Attribute that represents the revealed resource cards.
     */
    private List<DrawableCard> faceUpResourceCard;
    /**
     * Attribute that represent the cards on top of the gold deck.
     */
    private GoldCard topGoldDeck;
    /**
     * Attribute that represents the card on top of the resource deck.
     */
    private DrawableCard topResourceDeck;

    /**
     * Constructor method for DeckView.
     */
    public DeckView() {
        this.faceUpGoldCard = null;
        this.commonObjective = null;
        this.faceUpResourceCard = null;
        this.topGoldDeck = null;
        this.topResourceDeck = null;
    }

    /**
     * Setter for attribute commonObjective.
     * @param commonObjective common objective cards
     */
    public void setCommonObjective(List<ObjectiveCard> commonObjective) {
        this.commonObjective = commonObjective;
    }

    /**
     * Setter for attribute faceUpGoldCard.
     * @param faceUpGoldCard new face up cards
     */
    public void setFaceUpGoldCard(List<GoldCard> faceUpGoldCard) {
        this.faceUpGoldCard = faceUpGoldCard;
    }

    /**
     * Setter for attribute faceUpResourceCard.
     * @param faceUpResourceCard new face up cards
     */
    public void setFaceUpResourceCard(List<DrawableCard> faceUpResourceCard) {
        this.faceUpResourceCard = faceUpResourceCard;
    }

    /**
     * Setter for attribute topGoldDeck.
     * @param topGoldDeck new top card of the gold deck
     */
    public void setTopGoldDeck(GoldCard topGoldDeck) {
        this.topGoldDeck = topGoldDeck;
    }

    /**
     * Setter for attribute topResourceDeck.
     * @param topResourceDeck new top card of the resource deck
     */
    public void setTopResourceDeck(DrawableCard topResourceDeck) {
        this.topResourceDeck = topResourceDeck;
    }

    // TODO meotodi per stampare le modifiche
}

