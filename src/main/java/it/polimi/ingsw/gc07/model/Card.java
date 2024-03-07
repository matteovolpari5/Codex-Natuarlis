package it.polimi.ingsw.gc07.model;

/**
 * abstract class that represents the generic cards of the game
 */
public abstract class Card {
    /**
     * attribute that shows the id of the card
     */
    private final int cardID;
    /**
     * attribute that shows the type of the card
     */
    private final CardType cardType;

    /**
     * constructor of the class Card
     * @param cardID : id of the card
     * @param cardType : type of the card
     */
    // regular constructor
    public Card(int cardID, CardType cardType) {
        this.cardID = cardID;
        this.cardType = cardType;
    }

    /**
     * getter method of the attribute cardID
     * @return this.cardID
     */
    public int getCardID(){
        return this.cardID;
    }

    /**
     * getter method of the attribute cardType
     * @return this.cardType
     */
    public CardType getCardType() {
        return this.cardType;
    }
}
