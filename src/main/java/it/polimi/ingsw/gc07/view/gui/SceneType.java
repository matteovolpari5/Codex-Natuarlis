package it.polimi.ingsw.gc07.view.gui;

public enum SceneType {
    LOBBY_SCENE("Codex Naturalis - Lobby", "/it/polimi/ingsw/gc07/fxml/lobbyScene.fxml"),
    PLAYER_SCENE("Codex Naturalis - Board", "/it/polimi/ingsw/gc07/fxml/playerScene.fxml"),
    OTHER_PLAYER_SCENE("Codex Naturalis - Other board", "/it/polimi/ingsw/gc07/fxml/otherPlayerScene.fxml"),
    GAME_ENDED_SCENE("Codex Naturalis - Game ended", "/it/polimi/ingsw/gc07/fxml/gameEndedScene.fxml");

    private final String title;
    private final String fxmlScene;

    SceneType(String title, String fxmlScene){
        this.title = title;
        this.fxmlScene = fxmlScene;
    }

    public String getTitle() {
        return title;
    }

    public String getFxmlScene() {
        return fxmlScene;
    }
}
