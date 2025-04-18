package carne.Ventanas;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.ImageView;
import carne.Clases.Personaje; // Importación de la nueva clase

public class EntradaController implements Initializable {

    private Personaje personaje;

    @FXML
    private AnchorPane rootPane;
    @FXML
    private ImageView ImagenPJ;  // El ImageView que representa al personaje

    @Override
public void initialize(URL url, ResourceBundle rb) {
    personaje = new Personaje(rootPane, ImagenPJ);
    configurarEventosTeclado();
    iniciarBucleJuego();
    rootPane.requestFocus();  // Solicita el foco para que el rootPane reciba eventos de teclado
    rootPane.setFocusTraversable(true);  // Asegúrate de que el rootPane es traversable
}

    private void configurarEventosTeclado() {
        rootPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                personaje.presionarTecla(KeyCode.LEFT);
            } else if (event.getCode() == KeyCode.RIGHT) {
                personaje.presionarTecla(KeyCode.RIGHT);
            } else if (event.getCode() == KeyCode.SPACE) {
                personaje.presionarTecla(KeyCode.SPACE);
            }
        });

        rootPane.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                personaje.soltarTecla(KeyCode.LEFT);
            } else if (event.getCode() == KeyCode.RIGHT) {
                personaje.soltarTecla(KeyCode.RIGHT);
            }
        });
    }

    private void iniciarBucleJuego() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                personaje.actualizar(now);
            }
        }.start();
    }
}
