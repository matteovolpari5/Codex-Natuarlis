module it.polimi.ingsw.gc07 {
    requires javafx.controls;
    requires javafx.fxml;


    opens it.polimi.ingsw.gc07 to javafx.fxml;
    exports it.polimi.ingsw.gc07;
}