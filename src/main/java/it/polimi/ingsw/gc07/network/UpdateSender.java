package it.polimi.ingsw.gc07.network;

import it.polimi.ingsw.gc07.model.cards.Card;
import it.polimi.ingsw.gc07.model_listeners.*;
import it.polimi.ingsw.gc07.updates.*;

import java.rmi.RemoteException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class UpdateSender {
    /**
     * UpdateSender singleton instance.
     */
    private static UpdateSender updateSender;


    private static BlockingDeque<VirtualView> listenerQueue;
    private static BlockingDeque<Update> updateQueue;


    private static ExecutorService updateExecutor;



    /**
     * Update sender constructor.
     */
    private UpdateSender() {
        listenerQueue = new LinkedBlockingDeque<>();
        updateQueue = new LinkedBlockingDeque<>();


    }

    /**
     * Getter method for UpdateSender singleton instance.
     * @return UpdateSender instance
     */
    public static UpdateSender getUpdateSender() {
        if(updateSender == null) {
            updateSender = new UpdateSender();
        }
        return updateSender;
    }

    public static synchronized void receiveChatMessageUpdate(ChatListener listener, ChatMessageUpdate update) {
        updateExecutor.submit(() -> {
            try {
                listener.receiveChatMessageUpdate(update);
            }catch(RemoteException e) {
                // will be detected by ping pong manager
            }
        });
    }

    public static synchronized void receiveScoreUpdate(BoardListener listener, ScoreUpdate update) {
        updateExecutor.submit(() -> {
            try {
                listener.receiveScoreUpdate(update);
            }catch(RemoteException e) {
                // will be detected by ping pong manager
            }
        });
    }

    public static synchronized void receiveStarterCardUpdate(GameFieldListener listener, StarterCardUpdate update) {
        updateExecutor.submit(() -> {
            try {
                listener.receiveStarterCardUpdate(update);
            }catch(RemoteException e) {
                // will be detected by ping pong manager
            }
        });
    }

    public static synchronized void receivePlacedCardUpdate(GameFieldListener listener, PlacedCardUpdate update) {
        updateExecutor.submit(() -> {
            try {
                listener.receivePlacedCardUpdate(update);
            }catch(RemoteException e) {
                // will be detected by ping pong manager
            }
        });
    }

    public static synchronized void receiveGameModelUpdate(GameListener listener, GameModelUpdate update) {
        updateExecutor.submit(() -> {
            try {
                listener.receiveGameModelUpdate(update);
            }catch(RemoteException e) {
                // will be detected by ping pong manager
            }
        });
    }

    public static synchronized void receiveDeckUpdate(GameListener listener, DeckUpdate update) {
        updateExecutor.submit(() -> {
            try {
                listener.receiveDeckUpdate(update);
            }catch(RemoteException e) {
                // will be detected by ping pong manager
            }
        });
    }

    public static synchronized void receiveCommandResultUpdate(GameListener listener, CommandResultUpdate update) {
        updateExecutor.submit(() -> {
            try {
                listener.receiveCommandResultUpdate(update);
            }catch(RemoteException e) {
                // will be detected by ping pong manager
            }
        });
    }

    public static synchronized void receivePlayersUpdate(GameListener listener, PlayersUpdate update) {
        updateExecutor.submit(() -> {
            try {
                listener.receivePlayersUpdate(update);
            }catch(RemoteException e) {
                // will be detected by ping pong manager
            }
        });
    }

    public static synchronized void receiveGameEndedUpdate(GameListener listener, GameEndedUpdate update) {
        updateExecutor.submit(() -> {
            try {
                listener.receiveGameEndedUpdate(update);
            }catch(RemoteException e) {
                // will be detected by ping pong manager
            }
        });
    }

    public static synchronized void receiveFullChatUpdate(VirtualView listener, FullChatUpdate update) {
        updateExecutor.submit(() -> {
            try {
                listener.receiveFullChatUpdate(update);
            }catch(RemoteException e) {
                // will be detected by ping pong manager
            }
        });
    }

    public static synchronized void receiveConnectionUpdate(PlayerListener listener, ConnectionUpdate update) {
        updateExecutor.submit(() -> {
            try {
                listener.receiveConnectionUpdate(update);
            }catch(RemoteException e) {
                // will be detected by ping pong manager
            }
        });
    }

    public static synchronized void receiveStallUpdate(PlayerListener listener, StallUpdate update) {
        updateExecutor.submit(() -> {
            try {
                listener.receiveStallUpdate(update);
            }catch(RemoteException e) {
                // will be detected by ping pong manager
            }
        });
    }

    public static synchronized void receiveFullGameFieldUpdate(VirtualView listener, FullGameFieldUpdate update) {
        updateExecutor.submit(() -> {
            try {
                listener.receiveFullGameFieldUpdate(update);
            }catch(RemoteException e) {
                // will be detected by ping pong manager
            }
        });
    }

    public static synchronized void receiveCardHandUpdate(PlayerListener listener, CardHandUpdate update) {
        updateExecutor.submit(() -> {
            try {
                listener.receiveCardHandUpdate(update);
            }catch(RemoteException e) {
                // will be detected by ping pong manager
            }
        });
    }

    public static synchronized void receiveStarterCardUpdate(VirtualView listener, StarterCardUpdate update) {
        updateExecutor.submit(() -> {
            try {
                listener.receiveStarterCardUpdate(update);
            }catch(RemoteException e) {
                // will be detected by ping pong manager
            }
        });
    }

    public static synchronized void receiveScoreUpdate(VirtualView listener, ScoreUpdate update) {
        updateExecutor.submit(() -> {
            try {
                listener.receiveScoreUpdate(update);
            }catch(RemoteException e) {
                // will be detected by ping pong manager
            }
        });
    }

    public static synchronized void receiveSecretObjectiveUpdate(PlayerListener listener, SecretObjectivesUpdate update) {
        updateExecutor.submit(() -> {
            try {
                listener.receiveSecretObjectivesUpdate(update);
            }catch(RemoteException e) {
                // will be detected by ping pong manager
            }
        });
    }
}
