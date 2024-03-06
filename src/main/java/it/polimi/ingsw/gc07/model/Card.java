package it.polimi.ingsw.gc07.model;

public abstract class Card {
    private int cardID;
    private CardType cardType;

    // regular constructor
    public Card(int cardID, CardType cardType) {
        this.cardID = cardID;
        this.cardType = cardType;
    }
    // TODO: Se rendiamo la classe immutabile non serve
    public Card(Card existingCard) {
        this.cardID = existingCard.cardID;
        this.cardType = existingCard.cardType;
    }
    public int getCardID(){
        return this.cardID;
    }
    public CardType getCardType() {
        return this.cardType;
    }
}
