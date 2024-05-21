module it.polimi.ingsw.gc07 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.rmi;

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
    exports it.polimi.ingsw.gc07.enumerations;
    opens it.polimi.ingsw.gc07.enumerations to javafx.fxml;
    exports it.polimi.ingsw.gc07.model.chat;
    opens it.polimi.ingsw.gc07.model.chat to javafx.fxml;
    exports it.polimi.ingsw.gc07.exceptions;
    exports it.polimi.ingsw.gc07.controller;
    opens it.polimi.ingsw.gc07.controller to javafx.fxml;
    opens it.polimi.ingsw.gc07.network.rmi to java.rmi;
    opens it.polimi.ingsw.gc07.network to java.rmi;
    exports it.polimi.ingsw.gc07.model_view;
    exports it.polimi.ingsw.gc07.game_commands;
    opens it.polimi.ingsw.gc07.game_commands to javafx.fxml;
    exports it.polimi.ingsw.gc07.model_listeners;
    exports it.polimi.ingsw.gc07.updates;
    exports it.polimi.ingsw.gc07.network;
    exports it.polimi.ingsw.gc07.model_view_listeners;
    exports it.polimi.ingsw.gc07.view;
    opens it.polimi.ingsw.gc07.view.gui.gui_controllers to javafx.fxml;
    exports it.polimi.ingsw.gc07.view.gui to javafx.graphics;
    exports it.polimi.ingsw.gc07.utils;
    opens it.polimi.ingsw.gc07.utils to javafx.fxml;

}