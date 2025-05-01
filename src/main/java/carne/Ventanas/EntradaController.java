package carne.Ventanas;

import carne.Clases.Camara;
import carne.Clases.Moneda;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.ImageView;
import carne.Clases.Personaje; // Importación de la nueva clase
import carne.Clases.Plataforma;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class EntradaController implements Initializable {

    private Personaje personaje;
    private Personaje personaje1;
    private List<Personaje> personajes;
    private Plataforma plataforma1;
    private Plataforma plataforma2;
    private Moneda moneda;
    List<Plataforma> plataformas;
    private List<Moneda> monedas = new ArrayList<>();

    Camara camara;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private ImageView ImagenPJ;  // El ImageView que representa al personaje
    @FXML
    private ImageView ImagenPJ1;

    @FXML
    private Pane PanePJ1;
    @FXML
    private Pane PanePJ2;
    @FXML
    private Pane Plataforma;
    @FXML
    private Pane Plataforma2;
    @FXML
    private Pane Moneda;
    @FXML
    private Pane Moneda1;
    @FXML
    private Pane Moneda2;
    @FXML
    private Pane Moneda3;
    @FXML
    private Pane Moneda4;
    @FXML
    private Pane Moneda5;
    //chica sprites
    Image DerPaso1Chica = new Image(getClass().getResource("/Imagenes/Chica/der paso1.png").toExternalForm());
    Image DerPaso2Chica = new Image(getClass().getResource("/Imagenes/Chica/der paso2.png").toExternalForm());
    Image IzqPaso1Chica = new Image(getClass().getResource("/Imagenes/Chica/izq paso1.png").toExternalForm());
    Image IzqPaso2Chica = new Image(getClass().getResource("/Imagenes/Chica/izq paso2.png").toExternalForm());
    Image DerIdleChica = new Image(getClass().getResource("/Imagenes/Chica/der idle.png").toExternalForm());
    Image IzqIdleChica = new Image(getClass().getResource("/Imagenes/Chica/izq idle.png").toExternalForm());
    Image TransicionIzqChica = new Image(getClass().getResource("/Imagenes/Chica/der transicion.png").toExternalForm());
    Image TransicionDerChica = new Image(getClass().getResource("/Imagenes/Chica/izq transicion.png").toExternalForm());

    //chico sprites
    Image DerPaso1Chico = new Image(getClass().getResource("/Imagenes/Chico/izq paso1.png").toExternalForm());
    Image DerPaso2Chico = new Image(getClass().getResource("/Imagenes/Chico/izq paso2.png").toExternalForm());
    Image IzqPaso1Chico = new Image(getClass().getResource("/Imagenes/Chico/der paso1.png").toExternalForm());
    Image IzqPaso2Chico = new Image(getClass().getResource("/Imagenes/Chico/der paso2.png").toExternalForm());
    Image DerIdleChico = new Image(getClass().getResource("/Imagenes/Chico/izq idle.png").toExternalForm());
    Image IzqIdleChico = new Image(getClass().getResource("/Imagenes/Chico/der idle.png").toExternalForm());
    Image TransicionIzqChico = new Image(getClass().getResource("/Imagenes/Chico/izq transicion.png").toExternalForm());
    Image TransicionDerChico = new Image(getClass().getResource("/Imagenes/Chico/der transicion.png").toExternalForm());

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        plataformas = new ArrayList();
        plataforma1 = new Plataforma(Plataforma);
        plataforma2 = new Plataforma(Plataforma2);
        plataformas.add(plataforma1);
        plataformas.add(plataforma2);
        personaje = new Personaje(PanePJ1, ImagenPJ, DerPaso1Chica, DerPaso2Chica,
                IzqPaso1Chica, IzqPaso2Chica, DerIdleChica,
                IzqIdleChica, TransicionIzqChica, TransicionDerChica);

        personaje.setPlataformas(plataformas);

        personaje1 = new Personaje(PanePJ2, ImagenPJ1, DerPaso1Chico, DerPaso2Chico,
                IzqPaso1Chico, IzqPaso2Chico, DerIdleChico,
                IzqIdleChico, TransicionIzqChico, TransicionDerChico);

        personaje1.setPlataformas(plataformas);

        configurarEventosTeclado();
        iniciarBucleJuego();
        rootPane.requestFocus();  // Solicita el foco para que el rootPane reciba eventos de teclado
        rootPane.setFocusTraversable(true);  //  rootPane es traversable

        // Crear la lista de personajes
        personajes = new ArrayList<>();
        personajes.add(personaje);
        personajes.add(personaje1);

        camara = new Camara(rootPane, personaje, personaje1);
        monedas.add(new Moneda(Moneda, personaje, personaje1, rootPane));
        monedas.add(new Moneda(Moneda1, personaje, personaje1, rootPane));
        monedas.add(new Moneda(Moneda2, personaje, personaje1, rootPane));
        monedas.add(new Moneda(Moneda3, personaje, personaje1, rootPane));
        monedas.add(new Moneda(Moneda4, personaje, personaje1, rootPane));
        monedas.add(new Moneda(Moneda5, personaje, personaje1, rootPane));
    }

    private void configurarEventosTeclado() {
        rootPane.setOnKeyPressed(event -> {
            // Para el personaje principal (usando las flechas)
            if (event.getCode() == KeyCode.LEFT) {
                personaje.presionarTecla(KeyCode.LEFT);
            } else if (event.getCode() == KeyCode.RIGHT) {
                personaje.presionarTecla(KeyCode.RIGHT);
            } else if (event.getCode() == KeyCode.UP) {  // Cambiar SPACE por UP
                personaje.presionarTecla(KeyCode.SPACE);  // Para saltar con la flecha arriba
            }

            // Para el personaje 2 (jugador 2) usando A, D, W
            if (event.getCode() == KeyCode.A) {
                personaje1.presionarTecla(KeyCode.LEFT); // A para mover a la izquierda
            } else if (event.getCode() == KeyCode.D) {
                personaje1.presionarTecla(KeyCode.RIGHT); // D para mover a la derecha
            } else if (event.getCode() == KeyCode.W) {
                personaje1.presionarTecla(KeyCode.SPACE); // W para saltar
            }
        });

        rootPane.setOnKeyReleased(event -> {
            // Para el personaje principal
            if (event.getCode() == KeyCode.LEFT) {
                personaje.soltarTecla(KeyCode.LEFT);
            } else if (event.getCode() == KeyCode.RIGHT) {
                personaje.soltarTecla(KeyCode.RIGHT);
            } else if (event.getCode() == KeyCode.UP) {  // Cambiar SPACE por UP
                personaje.soltarTecla(KeyCode.SPACE);  // Para que deje de saltar cuando se suelta la flecha arriba
            }

            // Para el personaje 2
            if (event.getCode() == KeyCode.A) {
                personaje1.soltarTecla(KeyCode.LEFT);
            } else if (event.getCode() == KeyCode.D) {
                personaje1.soltarTecla(KeyCode.RIGHT);
            }
        });
    }

    private void iniciarBucleJuego() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                personaje.actualizar(now);
                personaje1.actualizar(now);
                camara.actualizar();
                for (Moneda m : monedas) {
                    m.actualizar();
                }

            }
        }.start();
    }
}
