package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.controller.enumerations.GameState;
import it.polimi.ingsw.gc07.exceptions.PlayerNotPresentException;
import it.polimi.ingsw.gc07.exceptions.WrongStateException;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.enumerations.TokenColor;

/**
 * Concrete command to add a pending player to an existing game.
 */
public class JoinExistingGameCommand implements GameCommand {
    /**
     * Reference to the games manager object.
     */
    private final GamesManager gamesManager;
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
     * @param gamesManager games manager
     * @param nickname nickname
     * @param tokenColor token color
     * @param gameId game ids
     */
    public JoinExistingGameCommand(GamesManager gamesManager, String nickname, TokenColor tokenColor, int gameId) {
        this.gamesManager = gamesManager;
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
        Player player = gamesManager.getPendingPlayer(nickname);
        if(player == null){
            //throw new PlayerNotPresentException();
            //TODO: no throws !!!
            // situazione tipo: il giocatore è già entrato in un gioco,
            // poi lancia di nuovo il comando per entrare in un gioco
            // cosa fare?
        }
        boolean found = false;
        for(Game game: gamesManager.getGames()) {
            if(game.getId() == gameId) {
                found = true;
                // check game state WAITING_PLAYERS
                if(!game.getState().equals(GameState.WAITING_PLAYERS)) {
                    //throw new WrongStateException();
                    //TODO: no throws !!!
                    // devo notificare, ma non posso mandare eccezione
                }
                // check token color unique
                if(!checkTokenColorUnique(game, tokenColor)) {
                    throw new RuntimeException();   // non abbiamo TokenColorException, ma tanto va tolta l'eccezione
                    //TODO: no throws !!!
                    // devo notificare, ma non posso mandare eccezione
                }
                player.setTokenColor(tokenColor);
                game.setCommand(new AddPlayerCommand(game, player));
                game.execute();
                gamesManager.getPendingPlayerspending().remove(player);
            }
        }
        if(!found){
            // TODO segnalarlo al player
        }
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
