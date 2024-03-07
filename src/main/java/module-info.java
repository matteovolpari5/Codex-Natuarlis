module it.polimi.ingsw.gc07 {
    requires javafx.controls;
    requires javafx.fxml;


    opens it.polimi.ingsw.gc07 to javafx.fxml;
    exports it.polimi.ingsw.gc07;
    exports it.polimi.ingsw.gc07.model;
    opens it.polimi.ingsw.gc07.model to javafx.fxml;
}