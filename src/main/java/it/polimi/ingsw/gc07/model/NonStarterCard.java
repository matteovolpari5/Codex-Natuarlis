package it.polimi.ingsw.gc07.model;

public class NonStarterCard extends PlaceableCard{
    private int placementScore;
    private Resource permanentResource;
    public NonStarterCard(int score,Resource resource,boolean[] front_corners,GameItem[] front_corners_content,Card newcard)
    {
        super(front_corners,front_corners_content,newcard);
        this.placementScore=score;
        this.permanentResource=resource;
    }
    private int getPlacementScore(){
        return this.placementScore;
    };
    private Resource getPermanentResource(){
        return this.permanentResource;
    };
}
