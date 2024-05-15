package it.polimi.ingsw.gc07.model_view;

import it.polimi.ingsw.gc07.enumerations.CardType;
import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model_view_listeners.DeckViewListener;

import java.util.ArrayList;
import java.util.List;

public class DeckView {
    /**
     * Attribute that represents the card on top of the resource deck.
     */
    private DrawableCard topResourceDeck;
    /**
     * Attribute that represent the cards on top of the gold deck.
     */
    private GoldCard topGoldDeck;
    /**
     * Attribute that represents the revealed resource cards.
     */
    private List<DrawableCard> faceUpResourceCard;
    /**
     * Attribute that represents the revealed gold cards.
     */
    private List<GoldCard> faceUpGoldCard;
    /**
     * Attribute that represents common objective cards.
     */
    private List<ObjectiveCard> commonObjective;

    private final List<DeckViewListener> deckViewListeners;

    /**
     * Constructor method for DeckView.
     */
    public DeckView() {
        this.faceUpGoldCard = null;
        this.commonObjective = null;
        this.faceUpResourceCard = null;
        this.topGoldDeck = null;
        this.topResourceDeck = null;
        this.deckViewListeners = new ArrayList<>();
    }

    /**
     * Method to register a deck view listener.
     * @param deckViewListener deck view listener
     */
    public void addListener(DeckViewListener deckViewListener) {
        deckViewListeners.add(deckViewListener);
    }

    /**
     * Method used to send a deck update.
     */
    private void sendDecksUpdate() {
        for(DeckViewListener l: deckViewListeners) {
            l.receiveDecksUpdate(topResourceDeck, topGoldDeck, faceUpResourceCard, faceUpGoldCard, commonObjective);
        }
    }

    /**
     * Setter for attribute topResourceDeck.
     * @param topResourceDeck new top card of the resource deck
     */
    public void setTopResourceDeck(DrawableCard topResourceDeck) {
        this.topResourceDeck = topResourceDeck;
        sendDecksUpdate();
    }

    /**
     * Setter for attribute topGoldDeck.
     * @param topGoldDeck new top card of the gold deck
     */
    public void setTopGoldDeck(GoldCard topGoldDeck) {
        this.topGoldDeck = topGoldDeck;
        sendDecksUpdate();
    }

    /**
     * Setter for attribute faceUpResourceCard.
     * @param faceUpResourceCard new face up cards
     */
    public void setFaceUpResourceCard(List<DrawableCard> faceUpResourceCard) {
        this.faceUpResourceCard = faceUpResourceCard;
        sendDecksUpdate();
    }

    /**
     * Setter for attribute faceUpGoldCard.
     * @param faceUpGoldCard new face up cards
     */
    public void setFaceUpGoldCard(List<GoldCard> faceUpGoldCard) {
        this.faceUpGoldCard = faceUpGoldCard;
        sendDecksUpdate();
    }

    /**
     * Setter for attribute commonObjective.
     * @param commonObjective common objective cards
     */
    public void setCommonObjective(List<ObjectiveCard> commonObjective) {
        this.commonObjective = commonObjective;
        sendDecksUpdate();
    }

    public DrawableCard getTopResourceDeck() {
        return topResourceDeck;
    }

    public GoldCard getTopGoldDeck() {
        return topGoldDeck;
    }

    public List<DrawableCard> getFaceUpResourceCard() {
        return faceUpResourceCard;
    }

    public List<GoldCard> getFaceUpGoldCard() {
        return faceUpGoldCard;
    }

    public List<ObjectiveCard> getCommonObjective() {
        return commonObjective;
    }

    public int getNumFaceUpCards(CardType cardType) {
        if(cardType.equals(CardType.RESOURCE_CARD))
            return faceUpResourceCard.size();
        else if(cardType.equals(CardType.GOLD_CARD))
            return faceUpGoldCard.size();
        return -1;
    }
}

