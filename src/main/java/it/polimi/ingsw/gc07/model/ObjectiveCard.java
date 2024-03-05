package it.polimi.ingsw.gc07.model;

import java.util.concurrent.locks.Condition;

public class ObjectiveCard extends Card{


    private Condition scoringCondition;
    private int objectiveScore;
    public ObjectiveCard(Condition scoring_condition,int objective_score, Card newcard)
    {
        super(newcard);
        this.scoringCondition=scoring_condition;
        this.objectiveScore=objective_score;
    }
    private void setScoringCondition(Condition condition){
        this.scoringCondition=condition;
    };
    private Condition getScoringCondition(){
        return this.scoringCondition;
    };
    private void setObjectiveScore(int score){
        this.objectiveScore=score;
    };
    private int getObjectiveScore(){
        return this.objectiveScore;
    };


}
