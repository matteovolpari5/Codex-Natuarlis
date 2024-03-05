package it.polimi.ingsw.gc07.model;


abstract class Card {
    private int cardID;
    private CardType cardType;

    public Card(Card newcard) {
        this.cardID= newcard.cardID;
        this.cardType=newcard.cardType;
    }

    public int getCardID(){
        return this.cardID;
    }
    public CardType getCardType() {
        return this.cardType;
    }
}
