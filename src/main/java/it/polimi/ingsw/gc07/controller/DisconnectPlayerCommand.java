package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.exceptions.PlayerAlreadyPresentException;
import it.polimi.ingsw.gc07.exceptions.PlayerNotPresentException;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.enumerations.GameState;

/**
 * Concrete command to disconnect a player from the game.
 */
public class DisconnectPlayerCommand implements GameCommand {
    /**
     * Game in which the command has to be executed.
     */
    Game game;
    /**
     * Nickname of the player that has disconnected.
     */
    String nickname;

    /**
     * Constructor of the concrete command DisconnectPlayerCommand.
     * @param game game in which the player has disconnected
     * @param nickname nickname of the player that has disconnected
     */
    public DisconnectPlayerCommand(Game game, String nickname) {
        this.game = game;
        this.nickname = nickname;
    }

    /**
     * Method to disconnect a player from the game.
     * @return command result
     */
    @Override
    //TODO il numero di connessi può scendere a 0
    // gestire attesa riconnessione/timeout
    // domanda su slack
    public CommandResult execute() {
        try{
            int pos = game.getPlayerByNickname(nickname);
            game.getPlayers().get(pos).setIsConnected(false);
            int numPlayersConnected = 0;
            for (Player p : game.getPlayers()){
                if (p.isConnected()){
                    numPlayersConnected++;
                }
            }
            if (numPlayersConnected <= 1){
                game.setState(GameState.WAITING_RECONNECTION);
            }
        }
        catch(PlayerNotPresentException e){
            return ConnectionResult.PLAYER_NOT_PRESENT;
        }
        return ConnectionResult.SUCCESS;
    }
}
