package it.polimi.ingsw.gc07.model.chat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrivateChatMessageTest {
    @Test void testReceiver() {
        PrivateChatMessage msg = new PrivateChatMessage("content", "sender", true, "receiver");
        assertEquals("content", msg.getContent());
        assertEquals("sender", msg.getSender());
        assertTrue(msg.getIsPublic());
        assertEquals("receiver", msg.getReceiver());
    }

}