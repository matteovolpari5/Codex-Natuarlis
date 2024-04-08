package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.controller.enumerations.CommandResult;
import it.polimi.ingsw.gc07.model.Player;

/**
 * Concrete command used to add a player to a list of pending players, that will be able
 * to enter a new or existing game.
 */
public class AddPlayerToPendingsCommand implements GameCommand {
    /**
     * Reference to the games manager object.
     */
    private final GamesManager gamesManager;
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
     * Constructor of AddPlayerToPendingsCommand.
     * @param nickname nickname of the player to add
     * @param connectionType connection type value of the player to add
     * @param interfaceType interface type value of the player to add
     */
    public AddPlayerToPendingsCommand(GamesManager gamesManager, String nickname, boolean connectionType, boolean interfaceType) {
        this.gamesManager = gamesManager;
        this.nickname = nickname;
        this.connectionType = connectionType;
        this.interfaceType = interfaceType;
    }

    /**
     * Execute method of the concrete command.
     * Accepts player data, creates a new player and adds it to the list of pending players.
     */
    @Override
    public void execute() {
        // this command can always be used
        if(checkNicknameUnique(nickname)){
            Player newPlayer = new Player(nickname, connectionType, interfaceType);
            gamesManager.getPendingPlayers().add(newPlayer);
        }
        else {
            gamesManager.getCommandResultManager().setCommandResult(CommandResult.PLAYER_ALREADY_PRESENT);
            return;
        }
        gamesManager.getCommandResultManager().setCommandResult(CommandResult.SUCCESS);
    }

    /**
     * Method to check if a nickname is unique or another player has the same nickname.
     * @param nickname nickname to check
     * @return true if no other player has the same nickname
     */
    private boolean checkNicknameUnique(String nickname) {
        boolean unique = true;
        for(Game g: gamesManager.getGames()) {
            if(g.hasPlayer(nickname)){
                unique = false;
            }
        }
        for(Player p: gamesManager.getPendingPlayers()) {
            if(p.getNickname().equals(nickname)) {
                unique = false;
            }
        }
        return unique;
    }
}
