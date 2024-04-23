package it.polimi.ingsw.gc07.model_view;

import java.util.ArrayList;
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
    private final List<PlayerView> playerViews;
    /**
     * scoreTrackBoardView reference
     */
    private final ScoreTrackBoardView scoreTrackBoardView;

    // i client contengono il riferimento a game view

    /**
     * Constructor of the class GameView
     */
    public GameView() {
        chatView = new ChatView();
        deckView = new DeckView();
        gameFieldView = new GameFieldView();
        playerViews = new ArrayList<>();
        scoreTrackBoardView = new ScoreTrackBoardView();
    }

    public void addPlayerView(PlayerView playerView) {
        playerViews.add(playerView);
    }
}
