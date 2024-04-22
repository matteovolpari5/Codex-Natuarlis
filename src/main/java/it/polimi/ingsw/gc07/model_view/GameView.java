package it.polimi.ingsw.gc07.model_view;

import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;

import java.util.List;

public class GameView {
    /**
     * chatView reference
     */
    private final ChatView chatView;
    /**
     * deckView reference
     */
    private final DeckView deckView;
    /**
     * gameFieldView reference
     */
    private final GameFieldView gameFieldView;
    /**
     * playerView reference
     */
    private final PlayerView  playerView;
    /**
     * scoreTrackBoardView reference
     */
    private final ScoreTrackBoardView scoreTrackBoardView;

    // i client contengono il riferimento a game view

    /**
     * Constructor of the class GameView
     * @param starterCard  starter card that has to be placed
     * @param faceUpGoldCard list of revealed gold card
     * @param commonObjective list of common objective
     * @param faceUpResourceCard list of revealed resource card
     * @param topGoldDeck card on the top of the gold deck
     * @param topResourceDeck card on the top of the gold deck
     * @param nickname nickname of the player
     */
    public GameView(PlaceableCard starterCard, List<GoldCard> faceUpGoldCard, List<ObjectiveCard> commonObjective, List<DrawableCard> faceUpResourceCard, GoldCard topGoldDeck, DrawableCard topResourceDeck,String nickname) {
        chatView = new ChatView();
        deckView = new DeckView(faceUpGoldCard, commonObjective, faceUpResourceCard, topGoldDeck, topResourceDeck);
        gameFieldView = new GameFieldView(starterCard);
        playerView = new PlayerView(nickname);
        scoreTrackBoardView = new ScoreTrackBoardView();
    }
}
