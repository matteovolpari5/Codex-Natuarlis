package it.polimi.ingsw.gc07.model;

import java.util.concurrent.locks.Condition;

public class ObjectiveCard extends Card{
    private final Condition scoringCondition;
    private final int objectiveScore;

    // regular constructor
    public ObjectiveCard(int cardID, CardType cardType, Condition scoringCondition, int objectiveScore) {
        super(cardID, cardType);
        this.scoringCondition = scoringCondition;
        this.objectiveScore = objectiveScore;
    }

    public Condition getScoringCondition(){
        return this.scoringCondition;
    };
    public int getObjectiveScore(){
        return this.objectiveScore;
    };
}
