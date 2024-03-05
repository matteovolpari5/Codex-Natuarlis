package it.polimi.ingsw.gc07.model;

public class GoldCard extends NonStarterCard{
    private Condition placementCondition;
    private Condition scoringCondition;
    public NonStarterCard(Condition placement_condition, Condition scoring_condition,int score,Resource resource,boolean[] front_corners,GameItem[] front_corners_content,Card newcard)
    {
        super(front_corners,front_corners_content,newcard);
        this.placementCondition=placement_condition;
        this.scoringCondition=scoring_condition;
    }
    private Condition getPlacementCondition(){
        return this.placementCondition;
    };
    private Condition getScoringCondition(){
        return this.scoringCondition;
    };
}
