module carne.juego {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    exports carne.juego;
    exports carne.Ventanas;
    
    opens carne.Ventanas to javafx.fxml;

}
