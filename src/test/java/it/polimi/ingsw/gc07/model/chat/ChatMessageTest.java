package it.polimi.ingsw.gc07.model.chat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChatMessageTest {
    @Test
    void testMsg() {
        ChatMessage msg = new ChatMessage("content", "sender", true);
        assertEquals("content", msg.getContent());
        assertEquals("sender", msg.getSender());
        assertTrue(msg.getIsPublic());
        assertNull(msg.getReceiver());
    }
}