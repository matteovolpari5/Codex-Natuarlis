package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.controller.enumerations.CommandResult;
import it.polimi.ingsw.gc07.controller.enumerations.GameState;
import it.polimi.ingsw.gc07.exceptions.PlayerNotPresentException;
import it.polimi.ingsw.gc07.model.Player;

/**
 * Concrete command to reconnect a player to the game.
 */
public class ReconnectPlayerCommand implements GameCommand {
    /**
     * Game in which the command has to be executed.
     */
    private final Game game;
    /**
     * Nickname of the player that will be reconnected to the game.
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
     */
    //TODO questione delle connessioni, se era sceso a 0?
    // vedi slack
    @Override
    public void execute() {
        if(!game.getPlayersGameField().containsKey(nickname)){
            game.getCommandResultManager().setCommandResult(CommandResult.PLAYER_NOT_PRESENT);
            return;
        }
        try{
            int pos = game.getPlayerByNickname(nickname);
            if(game.getPlayers().get(pos).isConnected()) {
                game.getCommandResultManager().setCommandResult(CommandResult.PLAYER_ALREADY_CONNECTED);
                return;
            }
            game.getPlayers().get(pos).setIsConnected(true);
            int numPlayersConnected = 0;
            for (Player p : game.getPlayers()){
                if (p.isConnected()){
                    numPlayersConnected++;
                }
            }
            if (numPlayersConnected == 1) {
                game.setState(GameState.WAITING_RECONNECTION);
                // TODO start the timer, when it ends, the only player connected wins
            }
            else if (numPlayersConnected > 1) {
                // players can re-start to play
                game.setState(GameState.PLAYING);
            }
        } catch (PlayerNotPresentException e) {
            game.getCommandResultManager().setCommandResult(CommandResult.PLAYER_NOT_PRESENT);
            return;
        }
        game.getCommandResultManager().setCommandResult(CommandResult.SUCCESS);
    }
}
