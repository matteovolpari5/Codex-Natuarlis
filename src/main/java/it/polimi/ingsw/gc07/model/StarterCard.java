package it.polimi.ingsw.gc07.model;

import java.util.List;
import java.util.ArrayList;

public class StarterCard extends PlaceableCard{
    private final List<GameResource> permanentResources;
    private final boolean[] backCorners;
    private final GameItem[] backCornersContent;

    public StarterCard(int cardID, CardType cardType, boolean[] frontCorners,
                       GameItem[] frontCornersContent, List<GameResource> permanentResources,
                       boolean[] backCorners, GameItem[] backCornersContent) {
        super(cardID, cardType, frontCorners, frontCornersContent);
        this.permanentResources = new ArrayList<>(permanentResources);
        boolean[] backCornersCopy = new boolean[4];
        for(int i = 0; i < 4; i++){
            backCornersCopy[i] = backCorners[i];
        }
        this.backCorners = backCornersCopy;
        GameItem[] backCornersContentCopy = new GameItem[4];
        for(int i = 0; i < 4; i++){
            backCornersContentCopy[i] = backCornersContent[i];
        }
        this.backCornersContent = backCornersContentCopy;
    }

    public  List<GameResource> getPermanentResources(){
        return new ArrayList<>(permanentResources);
    }
    public boolean[] getBackCorners()
    {
        boolean[] backCornersCopy = new boolean[4];
        for(int i = 0; i < 4; i++){
            backCornersCopy[i] = backCorners[i];
        }
        return backCornersCopy;
    }
    public GameItem[] getBackCornersContent()
    {
        GameItem[] backCornersContentCopy = new GameItem[4];
        for(int i = 0; i < 4; i++){
            backCornersContentCopy[i] = backCornersContent[i];
        }
        return backCornersContentCopy;
    }
}
