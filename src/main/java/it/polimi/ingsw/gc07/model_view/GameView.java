package it.polimi.ingsw.gc07.model_view;

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
     * @param nickname nickname of the player
     */
    public GameView(String nickname) {
        chatView = new ChatView();
        deckView = new DeckView();
        gameFieldView = new GameFieldView();
        playerView = new PlayerView(nickname);
        scoreTrackBoardView = new ScoreTrackBoardView();
    }
}
