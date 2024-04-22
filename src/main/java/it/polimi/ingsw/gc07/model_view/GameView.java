package it.polimi.ingsw.gc07.model_view;

import it.polimi.ingsw.gc07.model.cards.DrawableCard;
import it.polimi.ingsw.gc07.model.cards.GoldCard;
import it.polimi.ingsw.gc07.model.cards.ObjectiveCard;
import it.polimi.ingsw.gc07.model.cards.PlaceableCard;

import java.util.List;

public class GameView {
    private final ChatView chatView;
    private final DeckView deckView;
    private final GameFieldView gameFieldView;
    private final PlayerView  playerView;
    private final ScoreTrackBoardView scoreTrackBoardView;

    // i client contengono il riferimento a game view
    public GameView(PlaceableCard starterCard, List<GoldCard> faceUpGoldCard, List<ObjectiveCard> commonObjective, List<DrawableCard> faceUpResourceCard, GoldCard topGoldDeck, DrawableCard topResourceDeck,String nickname) {
        chatView = new ChatView();
        deckView = new DeckView(faceUpGoldCard, commonObjective, faceUpResourceCard, topGoldDeck, topResourceDeck);
        gameFieldView = new GameFieldView(starterCard);
        playerView = new PlayerView(nickname);
        scoreTrackBoardView = new ScoreTrackBoardView();
    }
}
