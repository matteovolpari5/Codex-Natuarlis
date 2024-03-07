package it.polimi.ingsw.gc07.model;

import java.util.concurrent.locks.Condition;

/**
 * class that represents the objective cards of the game
 */
public class ObjectiveCard extends Card{
    /**
     * attribute that shows the condition of the objectiveCard
     */
    private final Condition scoringCondition;
    /**
     * attribute that shows how many points will the player score if he achieves the objective
     */
    private final int objectiveScore;

    /**
     * constructor of the class ObjectiveCard
     * @param cardID : id of the card
     * @param cardType : type of the card
     * @param scoringCondition : condition of the card
     * @param objectiveScore : point of the card
     */
    // regular constructor
    public ObjectiveCard(int cardID, CardType cardType, Condition scoringCondition, int objectiveScore) {
        super(cardID, cardType);
        this.scoringCondition = scoringCondition;
        this.objectiveScore = objectiveScore;
    }

    /**
     * getter method of the attribute scoringCondition
     * @return this.scoringCondition
     */
    public Condition getScoringCondition(){
        return this.scoringCondition;
    };

    /**
     * getter method of the attribute objectiveScore
     * @return this.objectiveScore
     */
    public int getObjectiveScore(){
        return this.objectiveScore;
    };
}
