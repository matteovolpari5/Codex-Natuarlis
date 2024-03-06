package it.polimi.ingsw.gc07.model;

import java.util.concurrent.locks.Condition;

public class ObjectiveCard extends Card{
    private Condition scoringCondition;
    private int objectiveScore;

    // regular constructor
    // TODO: riferimento a scoringCondition dall'esterno! Immutabile?
    public ObjectiveCard(int cardID, CardType cardType, Condition scoringCondition, int objectiveScore) {
        super(cardID, cardType);
        this.scoringCondition = scoringCondition;
        this.objectiveScore = objectiveScore;
    }
    // TODO: da eliminare se rendiamo Card e sottoclassi immutabili
    // TODO: riferimento a scoringCondition dall'esterno! Immutabile?
    public ObjectiveCard(Card existingCard, Condition scoringCondition,int objectiveScore)
    {
        super(existingCard);
        this.scoringCondition = scoringCondition;
        this.objectiveScore = objectiveScore;
    }

    // TODO: restituisco riferimento a scoringCondition! Immutabile?
    private Condition getScoringCondition(){
        return this.scoringCondition;
    };
    private int getObjectiveScore(){
        return this.objectiveScore;
    };
}
