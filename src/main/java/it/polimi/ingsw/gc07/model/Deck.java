package it.polimi.ingsw.gc07.model;

import java.util.List;
import java.util.ArrayList;

public class Deck {
    private CardType deckType;
    private List<Card> deckContent;
    private Card[] faceUpCard;

    public Deck(CardType deckType, List<Card> deckContent, Card[] faceUpCard) {
        this.deckType = deckType;
        // Per crearere i deck, devono tenere conto di deckType!
        // TODO: modificare il costruttore!
        this.deckContent = new ArrayList<Card>(deckContent);
        this.faceUpCard = new Card[2];
        // this.faceUpCard[0] = new
        // this.faceUpCard[1] = new
    }

    // Nota: per tutti i metodi che restituiscono una carta
    // se non immutabile, restituire una copia
    public PlaceableCard drawDeckCard(){}
    public PlaceableCard drawFaceUpCard(int cardPos){}
    public Card revealFaceUpCard(int faceUpCardPos){}
    public GameResource revealBackDeckCard(){}
}
