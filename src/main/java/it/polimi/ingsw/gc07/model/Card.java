package it.polimi.ingsw.gc07.model;

public abstract class Card {
    private final int cardID;
    private final CardType cardType;

    // regular constructor
    public Card(int cardID, CardType cardType) {
        this.cardID = cardID;
        this.cardType = cardType;
    }

    public int getCardID(){
        return this.cardID;
    }
    public CardType getCardType() {
        return this.cardType;
    }
}
