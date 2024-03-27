package it.polimi.ingsw.gc07.model.chat;

import it.polimi.ingsw.gc07.exceptions.EmptyChatException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        assertThrows(EmptyChatException.class,
                () ->  chat.getLastMessage(receiver));
    }
}