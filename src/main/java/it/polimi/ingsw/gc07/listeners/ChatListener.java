package it.polimi.ingsw.gc07.listeners;

import it.polimi.ingsw.gc07.model.chat.Message;

public interface ChatListener {
    void showNewMessage(Message message);   // Message è immutabile, posso passare direttamente?

    // distinguo per private e public
    // oppure, se private, controllo se mandare
}
