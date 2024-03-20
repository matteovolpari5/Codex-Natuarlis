package it.polimi.ingsw.gc07.model.cards;

import it.polimi.ingsw.gc07.model.enumerations.CardType;

import it.polimi.ingsw.gc07.model.conditions.*;

/**
 * Abstract class that represents the generic cards of the game
 */
public abstract class Card {
    /**
     * Attribute that shows the id of the card
     */
    private final int id;
    /**
     * Attribute that shows the type of the card
     */
    private final CardType type;

    /**
     * Constructor of the class Card
     * @param id : id of the card
     * @param type : type of the card
     */
    public Card(int id, CardType type) {
        this.id = id;
        this.type = type;
    }

    /**
     * Getter method of the attribute id
     * @return this.id
     */
    public int getId(){
        return this.id;
    }

    /**
     * Getter method of the attribute type
     * @return this.type
     */
    public CardType getType() {
        return this.type;
    }
}
