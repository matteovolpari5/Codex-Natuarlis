package it.polimi.ingsw.gc07.model;

public class PlaceableCard extends Card{
    private boolean[] frontCorners;
    private GameItem[] frontCornersContent;
    public PlaceableCard(boolean[] front_corners,GameItem[] front_corners_content,Card newcard)
    {
        super(newcard);
        this.frontCorners=new boolean[4];
        this.frontCornersContent=new GameItem[4];
        for(int i=0;i<4;i++)
        {
            frontCorners[i]=front_corners[i];
            frontCornersContent[i]= new GameItem(front_corners_content[i]);
        }
    }

    public boolean[] getFrontCorners(){
        return this.frontCorners;
    }
    public GameItem[] getFrontCornersContent(){
        return this.frontCornersContent;
    }

}
