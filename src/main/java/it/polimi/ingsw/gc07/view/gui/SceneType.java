package it.polimi.ingsw.gc07.view.gui;

/**
 * Enum class representing all the scenes of the user interface.
 */
public enum SceneType {
    LOBBY_SCENE("Codex Naturalis - Lobby", "/it/polimi/ingsw/gc07/fxml/lobbyScene.fxml"),
    PLAYER_SCENE("Codex Naturalis - Board", "/it/polimi/ingsw/gc07/fxml/playerScene.fxml"),
    OTHER_PLAYER_SCENE("Codex Naturalis - Other board", "/it/polimi/ingsw/gc07/fxml/otherPlayerScene.fxml"),
    GAME_ENDED_SCENE("Codex Naturalis - Game ended", "/it/polimi/ingsw/gc07/fxml/gameEndedScene.fxml");

    /**
     * Title associated to the scene.
     */
    private final String title;
    /**
     * Fxml file path associated to the scene.
     */
    private final String fxmlScene;

    /**
     * Constructor.
     * @param title title associated to the scene
     * @param fxmlScene fxml file path associated to the scene
     */
    SceneType(String title, String fxmlScene){
        this.title = title;
        this.fxmlScene = fxmlScene;
    }

    /**
     * Getter method for the title.
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Getter method for fxml file path.
     * @return fxml file path
     */
    public String getFxmlScene() {
        return fxmlScene;
    }
}
