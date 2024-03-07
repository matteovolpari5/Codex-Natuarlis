package it.polimi.ingsw.gc07.model;

public abstract class PlaceableCard extends Card{
    private final boolean[] frontCorners;
    private final GameItem[] frontCornersContent;

    // regular constructor
    public PlaceableCard(int cardID, CardType cardType, boolean[] frontCorners, GameItem[] frontCornersContent) {
        super(cardID, cardType);
        boolean[] frontCornersCopy = new boolean[4];
        for(int i = 0; i < 4; i++){
            frontCornersCopy[i] = frontCorners[i];
        }
        GameItem[] frontCornersContentCopy = new GameItem[4];
        for(int i = 0; i < 4; i++){
            frontCornersContentCopy[i] = frontCornersContent[i];
        }
        this.frontCorners = frontCornersCopy;
        this.frontCornersContent = frontCornersContentCopy;
    }

    public boolean[] getFrontCorners(){
        boolean[] frontCornersCopy = new boolean[4];
        for(int i = 0; i < 4; i++){
            frontCornersCopy[i] = frontCorners[i];
        }
        return frontCornersCopy;
    }
    public GameItem[] getFrontCornersContent(){
        GameItem[] frontCornersContentCopy = new GameItem[4];
        for(int i = 0; i < 4; i++){
            frontCornersContentCopy[i] = frontCornersContent[i];
        }
        return frontCornersContentCopy;
    }
}
