package it.polimi.ingsw.gc07.model;

abstract class Card {
    private int cardID;
    private CardType cardType;

    public Card(Card newCard) {
        this.cardID= newCard.cardID;
        this.cardType=newCard.cardType;
    }

    public int getCardID(){
        return this.cardID;
    }
    public CardType getCardType() {
        return this.cardType;
    }
}
