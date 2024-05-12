package it.polimi.ingsw.gc07.network.ping_receiver;

import it.polimi.ingsw.gc07.controller.GameController;
import it.polimi.ingsw.gc07.model.Player;

public class PingReceiverGame extends PingReceiver {
    /**
     * Game Controller.
     */
    private final GameController gameController;

    /**
     * Constructor of PingReceiver.
     * @param gameController game controller
     */
    public PingReceiverGame(GameController gameController) {
        super();
        this.gameController = gameController;
    }
    @Override
    public void checkPing(String nickname) {
        int missedPing = 0;
        while(true) {
            synchronized(gameController) {
                if(getPlayerPing().get(nickname)) {
                    missedPing = 0;
                    if(!gameController.isPlayerConnected(nickname)) {
                        System.out.println("PRG> Reconnection detected " + nickname);
                        Player player = gameController.getPlayerByNickname(nickname);
                        assert (player != null);
                        gameController.reconnectPlayerOldSettings(nickname);
                        gameController.addListener(getPlayerVirtualViews().get(nickname));
                        if (player.getConnectionType()) {
                            // RMI
                            //TODO due istruzioni presenti uguali sia per socket che per RMI
                            System.out.println("PRG> Try to reconnect");
                        } else {
                            // TODO socket

                            System.out.println("PRG> Try to reconnect");
                        }
                    }
                }else {
                    missedPing ++;
                    System.out.println(missedPing);
                    if(missedPing >= getMaxMissedPings() && gameController.isPlayerConnected(nickname)) {
                        System.out.println("PRG> Disconnection detected " + nickname);
                        gameController.disconnectPlayer(nickname); // TODO metodo deve synchronized
                    }
                }
                getPlayerPing().put(nickname, false);
            }
            try {
                Thread.sleep(1000); // wait one second between two ping
            } catch (InterruptedException e) {
                // TODO
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        // TODO invio pong
    }
}
