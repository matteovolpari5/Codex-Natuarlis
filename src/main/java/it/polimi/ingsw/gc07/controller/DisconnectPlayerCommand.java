package it.polimi.ingsw.gc07.controller;

import it.polimi.ingsw.gc07.controller.enumerations.CommandResult;
import it.polimi.ingsw.gc07.controller.enumerations.GameState;
import it.polimi.ingsw.gc07.exceptions.PlayerNotPresentException;
import it.polimi.ingsw.gc07.model.Player;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Concrete command to disconnect a player from the game.
 */
public class DisconnectPlayerCommand extends GameCommand {
    /**
     * Nickname of the player that has disconnected.
     */
    String nickname;
    private boolean reconnectionOccurred;

    /**
     * Constructor of the concrete command DisconnectPlayerCommand.
     * This constructor takes parameter game, used by the server.
     * @param game game in which the player has disconnected
     * @param nickname nickname of the player that has disconnected
     */
    public DisconnectPlayerCommand(Game game, String nickname) {
        setGame(game);
        this.nickname = nickname;
    }

    /**
     * Constructor of the concrete command DisconnectPlayerCommand.
     * This constructor doesn't take a game as parameter, used by the client.
     * @param nickname nickname of the player that has disconnected
     */
    public DisconnectPlayerCommand(String nickname) {
        setGame(null);
        this.nickname = nickname;
    }

    /**
     * Method to disconnect a player from the game.
     */
    @Override
    public void execute() {
        Game game = getGame();

        // this command can always be used
        if(!game.getPlayersGameField().containsKey(nickname)){
            game.getCommandResultManager().setCommandResult(CommandResult.PLAYER_NOT_PRESENT);
            return;
        }
        try{
            int pos = game.getPlayerByNickname(nickname);
            if(!game.getPlayers().get(pos).isConnected())
            {
                game.getCommandResultManager().setCommandResult(CommandResult.PLAYER_ALREADY_DISCONNECTED);
                return;
            }
            game.getPlayers().get(pos).setIsConnected(false);
            int numPlayersConnected = game.getNumPlayersConnected();
            if (numPlayersConnected == 1){
                game.setState(GameState.WAITING_RECONNECTION);
                reconnectionOccurred = false;
                // TODO start the timer, when it ends, the only player left wins
                Timer timeout = new Timer();
                timeout.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (!reconnectionOccurred) {
                            timeout.cancel();
                            timeout.purge();
                            //TODO settare il player rimasto come vincitore
                        }
                    }
                }, 60*1000); //timeout of 1 minute
                new Thread(() -> {
                    for (int i = 0; i < 60; i++) {
                        try {
                            Thread.sleep(1000); // wait one second for each iteration
                        } catch (InterruptedException e) {
                            throw new RuntimeException();
                        }
                        if (game.getNumPlayersConnected() > 1) {
                            reconnectionOccurred = true;
                            timeout.cancel(); // it stops the timeout
                            timeout.purge();
                            break;
                        }
                    }
                }).start();
            } else if (numPlayersConnected == 0) {
                game.setState(GameState.NO_PLAYERS_CONNECTED);
                // TODO start the timer, when it ends, the game ends without winner
            }
        }
        catch(PlayerNotPresentException e){
            game.getCommandResultManager().setCommandResult(CommandResult.PLAYER_NOT_PRESENT);
            return;
        }
        game.getCommandResultManager().setCommandResult(CommandResult.SUCCESS);
    }
}
