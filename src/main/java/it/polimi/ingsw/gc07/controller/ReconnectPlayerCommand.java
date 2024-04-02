package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.exceptions.PlayerNotPresentException;
import it.polimi.ingsw.gc07.model.Player;
import it.polimi.ingsw.gc07.model.enumerations.GameState;

public class ReconnectPlayerCommand implements GameCommand{
    /**
     * Game in which the command has to be executed.
     */
    private final Game game;
    /**
     * nickname of the player that will be reconnected to the game
     */
    private final String nickname;
    /**
     * Constructor of the concrete command.
     * @param game game
     * @param nickname nickname of the player to reconnect
     */
    public ReconnectPlayerCommand(Game game, String nickname)
    {
        this.game=game;
        this.nickname=nickname;
    }
    /**
     * Method to execute the concrete command reconnectPlayerCommand.
     * @return command result
     */
    @Override
    public CommandResult execute() {
        try{
            int pos = game.getPlayerByNickname(nickname);
            game.getPlayers().get(pos).setIsConnected(true);
            int numPlayersConnected = 0;
            for (Player p : game.getPlayers()){
                if (p.isConnected()){
                    numPlayersConnected++;
                }
            }
            if (numPlayersConnected > 1){
                game.setState(GameState.PLAYING);
            }
        } catch (PlayerNotPresentException e) {
            return ConnectionResult.PLAYER_NOT_PRESENT;
        }
        return ConnectionResult.SUCCESS;
    }
}
