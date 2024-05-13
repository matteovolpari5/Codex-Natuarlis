package it.polimi.ingsw.gc07.network.ping_receiver;

import it.polimi.ingsw.gc07.controller.GamesManager;

public class PingReceiverGamesManager extends PingReceiver{
    GamesManager gamesManager;

    public PingReceiverGamesManager(GamesManager gamesManager) {
        super();
        this.gamesManager = gamesManager;
    }

    @Override
    public void checkPing(String nickname) {
        int missedPing = 0;
        while(true) {
            synchronized(gamesManager) {
                if(getPlayerPing().get(nickname)) {
                    missedPing = 0;
                }else {
                    missedPing ++;
                    System.out.println(missedPing);
                    if(missedPing >= getMaxMissedPings()) {
                        System.out.println("PRGM> Disconnection detected " + nickname);
                        gamesManager.removeFromPending(nickname); // TODO metodo deve synchronized
                        break;
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
