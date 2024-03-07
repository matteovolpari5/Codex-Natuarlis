package it.polimi.ingsw.gc07.model;

/**
 * Abstract class that represents the generic cards of the game
 */
public abstract class Card {
    /**
     * Attribute that shows the id of the card
     */
    private final int cardID;
    /**
     * Attribute that shows the type of the card
     */
    private final CardType cardType;

    /**
     * Constructor of the class Card
     * @param cardID : id of the card
     * @param cardType : type of the card
     */
    public Card(int cardID, CardType cardType) {
        this.cardID = cardID;
        this.cardType = cardType;
    }

    /**
     * Getter method of the attribute cardID
     * @return this.cardID
     */
    public int getCardID(){
        return this.cardID;
    }

    /**
     * Getter method of the attribute cardType
     * @return this.cardType
     */
    public CardType getCardType() {
        return this.cardType;
    }
}
