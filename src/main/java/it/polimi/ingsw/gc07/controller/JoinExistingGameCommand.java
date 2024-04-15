package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.controller.enumerations.CommandResult;
import it.polimi.ingsw.gc07.controller.enumerations.GameState;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;

/**
 * Concrete command to add a pending player to an existing game.
 */
public class JoinExistingGameCommand extends GamesManagerCommand {
    /**
     * Nickname of the player to add.
     */
    private final String nickname;
    /**
     * Token color chosen from the pending player.
     */
    private final TokenColor tokenColor;
    /**
     * Game id of the game to join.
     */
    private final int gameId;

    /**
     * Constructor of the concrete command JoinExistingGameCommand.
     * This constructor takes games manager as parameter, used by the server.
     * @param gamesManager games manager
     * @param nickname nickname
     * @param tokenColor token color
     * @param gameId game ids
     */
    public JoinExistingGameCommand(GamesManager gamesManager, String nickname, TokenColor tokenColor, int gameId) {
        setGamesManager(gamesManager);
        this.nickname = nickname;
        this.tokenColor = tokenColor;
        this.gameId = gameId;
    }

    /**
     * Constructor of the concrete command JoinExistingGameCommand.
     * This constructor doesn't take games manager as parameter, used by the client.
     * @param nickname nickname
     * @param tokenColor token color
     * @param gameId game ids
     */
    public JoinExistingGameCommand(String nickname, TokenColor tokenColor, int gameId) {
        setGamesManager(null);
        this.nickname = nickname;
        this.tokenColor = tokenColor;
        this.gameId = gameId;
    }

    /**
     * Execute method of the concrete command.
     * Allows to add a player to an existing game.
     */
    @Override
    public void execute() {
        GamesManager gamesManager = getGamesManager();

        // this command can always be used
        Player player = gamesManager.getPendingPlayer(nickname);
        if(player == null){
            gamesManager.getCommandResultManager().setCommandResult(CommandResult.PLAYER_NOT_PRESENT);
            return;
        }
        boolean found = false;
        for(Game game: gamesManager.getGames()) {
            if(game.getId() == gameId) {
                found = true;
                // check game state WAITING_PLAYERS
                if(!game.getState().equals(GameState.GAME_STARTING)) {
                    gamesManager.getCommandResultManager().setCommandResult(CommandResult.GAME_FULL);
                    return;
                }
                // check token color unique
                if(!checkTokenColorUnique(game, tokenColor)) {
                    gamesManager.getCommandResultManager().setCommandResult(CommandResult.TOKEN_COLOR_ALREADY_TAKEN);
                    return;
                }
                player.setTokenColor(tokenColor);
                game.setAndExecuteCommand(new AddPlayerCommand(game, player));
                gamesManager.getPendingPlayers().remove(player);
            }
        }
        if(!found){
            gamesManager.getCommandResultManager().setCommandResult(CommandResult.GAME_NOT_PRESENT);
            return;
        }
        // join successful, but it is necessary to set the game for the client
        //TODO va bene per Socket ???
        // altrimenti possiamo mettere una seconda bandierina booleana che indica se serve settare il game
        gamesManager.getCommandResultManager().setCommandResult(CommandResult.SET_GAME);
    }

    /**
     * Method to check if a given token color is unique in a game.
     * @param game game
     * @param tokenColor token color
     * @return true if the token color is unique in the game
     */
    private boolean checkTokenColorUnique(Game game, TokenColor tokenColor) {
        boolean unique = true;
        for(Player p: game.getPlayers()) {
            if(p.getTokenColor().equals(tokenColor))
                unique = false;
        }
        return unique;
    }
}
