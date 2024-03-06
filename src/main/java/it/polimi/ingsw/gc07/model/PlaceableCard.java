package it.polimi.ingsw.gc07.model;

public class PlaceableCard extends Card{
    private boolean[] frontCorners;
    private GameItem[] frontCornersContent;

    // regular constructor
    // TODO: riferimento a frontCorners e frontCornersContent dall'esterno! Immutabile?
    public PlaceableCard(int cardID, CardType cardType, boolean[] frontCorners, GameItem[] frontCornersContent) {
        super(cardID, cardType);
        this.frontCorners = frontCorners;
        this.frontCornersContent = frontCornersContent;
    }

    // TODO: da eliminare se rendiamo Card e sottoclassi immutabili
    public PlaceableCard(Card existingCard, boolean[] frontCorners, GameItem[] frontCornersContent)
    {
        super(existingCard);
        this.frontCorners = new boolean[4];
        this.frontCornersContent = new GameItem[4];
        for(int i = 0; i < 4; i++)
        {
            this.frontCorners[i] = frontCorners[i];
            // TODO: SBAGLIATO, serve istanziare in base a GameResource o GameObject
            // this.frontCornersContent[i] = new GameItem(frontCornersContent[i]);
        }
    }
    // TODO: sta facendo sfuggire riferimenti, potrebbe essere un problema!!!
    public boolean[] getFrontCorners(){
        return this.frontCorners;
    }
    public GameItem[] getFrontCornersContent(){
        return this.frontCornersContent;
    }

}
