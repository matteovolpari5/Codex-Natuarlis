package it.polimi.ingsw.gc07.network.rmi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RmiServerGamesManagerTest {

    @Test
    void singletonTest() {
        RmiServerGamesManager gm1 = RmiServerGamesManager.getRmiServerGamesManager();
        RmiServerGamesManager gm2 = RmiServerGamesManager.getRmiServerGamesManager();
        assertSame(gm1, gm2);
    }

}