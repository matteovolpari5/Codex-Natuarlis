package it.polimi.ingsw.gc07.model.decks;
import it.polimi.ingsw.gc07.exceptions.CardNotPresentException;
import java.util.List;

/**
 * Class representing decks used during the whole game.
 * Objective cards deck is PlayingDeck object.
 */
public class PlayingDeck<T> extends Deck<T> {
    /**
     * Array containing the two face up cards that players can draw.
     */
    List<T> faceUpCards;

    /**
     * Constructor class PlayingDeck.
     */
    public PlayingDeck() {
        super();
        this.faceUpCards = null;
    }

    public void setFaceUpCards(List<T> faceUpCards){
        this.faceUpCards = faceUpCards;
    }

    /**
     * Method that allows a player to see the card in position cardPos
     * without drawing it.
     * @param cardPos position of the card the player wants to see
     * @return face up card in position cardPos
     */
    public T revealFaceUpCard(int cardPos) throws IndexOutOfBoundsException, CardNotPresentException {
        if(cardPos >= faceUpCards.size()){
            throw new IndexOutOfBoundsException();
        }
        if(faceUpCards.get(cardPos) == null){
            throw new CardNotPresentException();
        }
        return faceUpCards.get(cardPos);
    }
}
