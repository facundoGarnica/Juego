package carne.juego;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage stage) {
        try {
            // Cargar el archivo FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Entrada.fxml"));
            AnchorPane root = fxmlLoader.load();

            // Obtener la resolución de la pantalla
            Screen screen = Screen.getPrimary();
            double width = screen.getVisualBounds().getWidth();  // Ancho de la pantalla
            double height = screen.getVisualBounds().getHeight(); // Alto de la pantalla

            // Crear la escena y agregarla al escenario
            Scene scene = new Scene(root, width, height); // Establecer el tamaño de la escena a pantalla completa
            stage.setTitle("Crear Ventas");
            stage.setScene(scene);

            // Establecer el tamaño de la ventana sin cambiar a pantalla completa
            stage.setMaximized(true); // Maximizar la ventana para que ocupe toda la pantalla

            stage.show(); // Mostrar el escenario
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading FXML file.");
        }
    }

    public static void main(String[] args) {
        launch();
    }

}