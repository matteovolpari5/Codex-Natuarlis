package it.polimi.ingsw.gc07.model;

import java.util.List;

public class StarterCard extends PlaceableCard{
    private List<Resource> permanentResources;
    private boolean[] backCorners;
    private GameItem[] backCornersContent;
    public StarterCard(List<Resource> permanent_res,boolean[] back_corners,GameItem[] back_corners_content,boolean[] front_corners,GameItem[] front_corners_content,Card newcard)
    {
        super(front_corners,front_corners_content,newcard);
        this.permanentResources = new List<Resource>();
        this.permanentResources.addAll(permanent_res);
        this.backCorners = new boolean[4];
        this.backCornersContent = new GameItem[4];
        for(int i =0; i <4;i++)
        {
            backCorners[i]=back_corners[i];
            backCornersContent[i]=new GameItem(back_corners_content[i]);
        }
    }
    public  List<Resource> getPermanentResources(){
        return this.permanentResources;
    }
    public boolean[] getBackCorners()
    {
        return this.backCorners;
    }
    public GameItem[] getBackCornersContent()
    {
        return this.backCornersContent;
    }
}
