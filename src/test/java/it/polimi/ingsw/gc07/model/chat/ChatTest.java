package it.polimi.ingsw.gc07.model.chat;

import it.polimi.ingsw.gc07.model_listeners.ChatListener;
import it.polimi.ingsw.gc07.network.rmi.RmiClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChatTest {
    Chat chat = null;

    @BeforeEach
    void setUp() {
        // create an empty chat
        chat = new Chat();
    }

    @Test
    void getLastMessageEmptyChat() {
        String receiver = "TestReceiver";
        assertNull(chat.getLastMessage(receiver));
    }

    @Test
    void getContentEmptyChat() {
        String receiver = "TestReceiver";
        assertEquals(0, chat.getContent(receiver).size());
    }

    @Test
    public void testPublicMessages() {
        String sender = "TestSender";
        String receiver1 = "TestReceiver1";
        String receiver2 = "TestReceiver2";
        List<String> players = new ArrayList<>();
        players.add(sender);
        players.add(receiver1);
        players.add(receiver2);
        chat.addPublicMessage("TestMessage1", sender);
        chat.addPublicMessage("TestMessage2", sender);
        chat.addPublicMessage("TestMessage3", sender);
        assertEquals(3, chat.getContent(receiver1).size());
        assertEquals(3, chat.getContent(receiver2).size());
        assertEquals("TestMessage3", chat.getLastMessage(receiver1).getContent());
        assertEquals("TestMessage3", chat.getLastMessage(receiver2).getContent());
    }

    @Test
    public void testPrivateMessages() {
        String sender = "TestSender";
        String receiver1 = "TestReceiver1";
        String receiver2 = "TestReceiver2";
        List<String> players = new ArrayList<>();
        players.add(sender);
        players.add(receiver1);
        players.add(receiver2);
        chat.addPublicMessage("TestMessage1", sender);
        chat.addPrivateMessage("TestMessage2", sender, receiver1);
        chat.addPrivateMessage("TestMessage3", sender, receiver1);
        assertEquals(3, chat.getContent(receiver1).size());
        assertEquals("TestMessage3", chat.getLastMessage(receiver1).getContent());
        assertEquals(1, chat.getContent(receiver2).size());
        assertEquals("TestMessage1", chat.getLastMessage(receiver2).getContent());
    }

    @Test
    public void testListener() {
        ChatListener listener1;
        try {
            listener1 = new RmiClient("P1", null);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        ChatListener listener2;
        try {
            listener2 = new RmiClient("P2", null);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        chat.addListener(listener1);
        chat.addListener(listener2);
        chat.addPublicMessage("content", "Player1");
        chat.addPrivateMessage("content", "Player1", "Player2");
    }
}