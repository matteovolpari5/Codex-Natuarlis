package it.polimi.ingsw.gc07.network;

import it.polimi.ingsw.gc07.ModelListener;
import it.polimi.ingsw.gc07.updates.*;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Class used by the model to send updates to clients on separate threads.
 * Every client has its own updates queue and thread in order not to block the model in case of disconnection.
 */
public class UpdateSender {
    /**
     * UpdateSender singleton instance.
     */
    private static UpdateSender updateSender;
    /**
     * Map containing the update blocking queue of clients.
     */
    private final Map<ModelListener, BlockingQueue<Update>> listenerQueues;

    /**
     * Update sender constructor.
     */
    private UpdateSender() {
        this.listenerQueues = new HashMap<>();
    }

    /**
     * Getter method for UpdateSender singleton instance.
     * @return UpdateSender instance
     */
    // no need to make it synchronized, because it is created by GamesManager
    // before any model thread can use this method
    public static UpdateSender getUpdateSender() {
        if(updateSender == null) {
            updateSender = new UpdateSender();
        }
        return updateSender;
    }

    /**
     * Method used to add a listener to UpdateSender, creates an update queue for the listener.
     * @param listener listener to add
     */
    public synchronized void addListenerQueue(ModelListener listener) {
        assert(listener != null && !listenerQueues.containsKey(listener));

        // create queue
        listenerQueues.put(listener, new LinkedBlockingQueue<>());
        // start thread
        new Thread(() -> sendListenerUpdates(listener)).start();
    }

    /**
     * Method used to remove a listener from UpdateSender, deletes his update queue.
     * @param listener listener to remove
     */
    public synchronized void removeListenerQueue(ModelListener listener) {
        assert(listener != null && listenerQueues.containsKey(listener));
        listenerQueues.remove(listener);
    }

    /**
     * Method used to get the updates queue of a client.
     * @param client client
     * @return client's update queue
     */
    // synchronized because it is used by all clients' threads
    private synchronized BlockingQueue<Update> getUpdatesQueue(ModelListener client) {
        if(listenerQueues.containsKey(client)) {
            return listenerQueues.get(client);
        }
        return null;
    }

    /**
     * Method executed in a separate thread for every client, takes updates
     * from the queue and sends them to its client.
     * @param listener client owner of the queue
     */
    private void sendListenerUpdates(ModelListener listener) {
        while(true) {
            // get client's update queue
            BlockingQueue<Update> updatesQueue = getUpdatesQueue(listener);
            if(updatesQueue == null) {
                // client disconnected
                break;
            }
            Update update;
            try {
                // wait for an update for 60 seconds
                // else return null
                update = updatesQueue.poll(60, TimeUnit.SECONDS);
            }catch(InterruptedException e) {
                throw new RuntimeException();
            }
            if(update != null) {
                try {
                    listener.receiveUpdate(update);
                } catch (RemoteException e) {
                    // will be detected by PingPongManager
                }
            }
        }
    }

    /**
     * Method used to add an update to a listener's queue.
     * @param listener listener
     * @param update update
     */
    public synchronized void receiveUpdate(ModelListener listener, Update update) {
        assert(listener != null && update != null && listenerQueues.containsKey(listener));
        try {
            listenerQueues.get(listener).put(update);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
