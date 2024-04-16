package it.polimi.ingsw.gc07.controller;

/**
 * Concrete command used to add a player to a list of pending players, that will be able
 * to enter a new or existing game.
 */
public class AddPlayerToPendingCommand extends GamesManagerCommand {
    /**
     * Nickname of the player to add.
     */
    private final String nickname;
    /**
     * Connection type value of the player to add.
     */
    private final boolean connectionType;
    /**
     * Interface type value of the player to add.
     */
    private final boolean interfaceType;

    /**
     * Constructor of AddPlayerToPendingCommand.
     * @param nickname nickname of the player to add
     * @param connectionType connection type value of the player to add
     * @param interfaceType interface type value of the player to add
     */
    public AddPlayerToPendingCommand(String nickname, boolean connectionType, boolean interfaceType) {
        this.nickname = nickname;
        this.connectionType = connectionType;
        this.interfaceType = interfaceType;
    }

    @Override
    public String getNickname() {
        return nickname;
    }

    /**
     * Execute method of the concrete command.
     * Accepts player data, creates a new player and adds it to the list of pending players.
     */
    @Override
    public void execute(GamesManager gamesManager) {
        gamesManager.addPlayerToPending(nickname, connectionType, interfaceType);
    }
}
