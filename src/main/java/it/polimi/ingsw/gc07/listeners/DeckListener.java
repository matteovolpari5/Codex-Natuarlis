package it.polimi.ingsw.gc07.listeners;

import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;

import java.util.List;

public interface DeckListener {
    /**
     * Method used to notify the client that the card on top the resource cards deck changed.
     * @param card new card on top of the deck
     */
    void topResourceCardChanged(DrawableCard card);

    /**
     * Method used to notify the client that the card on top the gold cards deck changed.
     * @param card new card on top of the deck
     */
    void topGoldCardChanged(GoldCard card);

    /**
     * Method used to notify the client that the resource face up cards changed.
     * @param faceUpCards new list of face up cards
     */
    void resourceFaceUpCardsChanged(List<DrawableCard> faceUpCards);

    /**
     * Method used to notify the client that the gold face up cards changed.
     * @param faceUpCards new list of face up cards
     */
    void goldFaceUpCardsChanged(List<GoldCard> faceUpCards);

    /**
     * Method used to notify the client that the objective face up cards changed.
     * @param faceUpCards new list of face up cards
     */
    void commonObjectiveCardsRevealed(List<ObjectiveCard> faceUpCards);
}
