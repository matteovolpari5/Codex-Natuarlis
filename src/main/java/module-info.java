module it.polimi.ingsw.gc07 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens it.polimi.ingsw.gc07 to javafx.fxml;
    exports it.polimi.ingsw.gc07;
    exports it.polimi.ingsw.gc07.model;
    opens it.polimi.ingsw.gc07.model to javafx.fxml;
    exports it.polimi.ingsw.gc07.model.decks;
    opens it.polimi.ingsw.gc07.model.decks to javafx.fxml;
    exports it.polimi.ingsw.gc07.model.cards;
    opens it.polimi.ingsw.gc07.model.cards to javafx.fxml;
    exports it.polimi.ingsw.gc07.model.conditions;
    opens it.polimi.ingsw.gc07.model.conditions to javafx.fxml;
    exports it.polimi.ingsw.gc07.model.enumerations;
    opens it.polimi.ingsw.gc07.model.enumerations to javafx.fxml;
    exports it.polimi.ingsw.gc07.model.chat;
    opens it.polimi.ingsw.gc07.model.chat to javafx.fxml;

    exports it.polimi.ingsw.gc07.exceptions;
    exports it.polimi.ingsw.gc07.controller;
    opens it.polimi.ingsw.gc07.controller to javafx.fxml;
}