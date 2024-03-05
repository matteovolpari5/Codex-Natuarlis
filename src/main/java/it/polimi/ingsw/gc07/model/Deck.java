package it.polimi.ingsw.gc07.model;

public class Deck {
    private CardType deckType;
    private List<Card> deckContent;
    private Card[] faceUpCard;

    public Deck(CardType deckType, List<Card> deckContent, Card[] faceUpCard) {
        this.deckType = deckType;
        this.deckContent = new List<Card>(deckContent);
        this.faceUpCard = new faceUpCard[2];
        this.faceUpCard[0] = new Card(faceUpCard[0]);
        this.faceUpCard[1] = new Card(faceUpCard[1]);
    }

    public PlaceableCard drawDeckCard();
    public PlaceableCard drawFaceUpCard(int);
    public Card revealFaceUpCard(int);
    public Resource revealBackDeckCard();
}
