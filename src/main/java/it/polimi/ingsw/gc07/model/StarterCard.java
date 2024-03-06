package it.polimi.ingsw.gc07.model;

import java.util.List;
import java.util.ArrayList;

public class StarterCard extends PlaceableCard{
    private List<GameResource> permanentResources;
    private boolean[] backCorners;
    private GameItem[] backCornersContent;

    public StarterCard(int cardID, CardType cardType, boolean[] frontCorners,
                       GameItem[] frontCornersContent, List<GameResource> permanentResources,
                       boolean[] backCorners, GameItem[] backCornersContent) {
        super(cardID, cardType, frontCorners, frontCornersContent);
        this.permanentResources = permanentResources;
        this.backCorners = backCorners;
        this.backCornersContent = backCornersContent;
    }

    public StarterCard(Card existingCard, boolean[] frontCorners, GameItem[] frontCornersContent,
                       List<GameResource> permanentResources,boolean[] backCorners,
                       GameItem[] backCornersContent) {
        super(existingCard, frontCorners, frontCornersContent);
        this.permanentResources = new ArrayList<GameResource>(permanentResources);
        this.backCorners = new boolean[4];
        this.backCornersContent = new GameItem[4];
        for(int i =0; i <4;i++)
        {
            this.backCorners[i]=backCorners[i];
            this.backCornersContent[i]=new GameItem(backCornersContent[i]);
        }
    }
    // TODO: sfugge riferimento List!
    public  List<GameResource> getPermanentResources(){
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
