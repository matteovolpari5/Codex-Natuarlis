package it.polimi.ingsw.gc07.listeners;

import it.polimi.ingsw.gc07.updates.CardHandUpdate;
import it.polimi.ingsw.gc07.updates.ConnectionUpdate;
import it.polimi.ingsw.gc07.updates.StallUpdate;

public interface PlayerListener {
    /**
     * Method used to send a stall update.
     * @param stallUpdate stall update
     */
    void receiveStallUpdate(StallUpdate stallUpdate);

    /**
     * Method used to send a connection update.
     * @param connectionUpdate connection update
     */
    void receiveConnectionUpdate(ConnectionUpdate connectionUpdate);

    /**
     * Method used to send a card hand update.
     * @param cardHandUpdate card hand update
     */
    void receiveCardHandUpdate(CardHandUpdate cardHandUpdate);
}
