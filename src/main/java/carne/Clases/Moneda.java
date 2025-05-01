package carne.Clases;

import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.util.Random;

public class Moneda {

    private Pane monedaPane;
    private Personaje personaje1;
    private Personaje personaje2;
    private Pane root;
    private double distancia = 15.0;  // Distancia de movimiento arriba y abajo
    private TranslateTransition transition;

    // MediaPlayer para el sonido
    private MediaPlayer mediaPlayer;

    public Moneda(Pane monedaPane, Personaje personaje1, Personaje personaje2, Pane root) {
        this.monedaPane = monedaPane;
        this.personaje1 = personaje1;
        this.personaje2 = personaje2;
        this.root = root;

        // Ruta del archivo de sonido (asegúrate de que la ruta sea correcta)
        String path = "sound/Monedas/recolectar.mp3";  // Ruta relativa desde resources
        // Cargar el recurso desde el classloader
        Media media = new Media(getClass().getClassLoader().getResource(path).toString());
        mediaPlayer = new MediaPlayer(media);

        Random random = new Random();
        double duracion = 1000 + random.nextInt(1000); // Duración entre 1 y 2 segundos

        transition = new TranslateTransition(Duration.millis(duracion), monedaPane);
        transition.setByY(-distancia);  // Mover hacia arriba
        transition.setAutoReverse(true);  // Volver al punto original
        transition.setCycleCount(TranslateTransition.INDEFINITE);  // Ciclo infinito
        transition.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);  // Movimiento suave
        transition.play();  // Iniciar animación
    }

    public void actualizar() {
        if (!root.getChildren().contains(monedaPane)) return;

        if (esColision(personaje1)) {
            personaje1.agregarPuntos(20);
            root.getChildren().remove(monedaPane);
            System.out.println("Moneda agarrada por jugador 1");
            playSound();  // Reproducir el sonido
            transition.stop();  // Detener animación
        } else if (esColision(personaje2)) {
            personaje2.agregarPuntos(20);
            root.getChildren().remove(monedaPane);
            System.out.println("Moneda agarrada por jugador 2");
            playSound();  // Reproducir el sonido
            transition.stop();  // Detener animación
        }
    }

    private boolean esColision(Personaje p) {
        return monedaPane.getBoundsInParent().intersects(p.getContenedor().getBoundsInParent());
    }

    // Método para reproducir el sonido
    private void playSound() {
        mediaPlayer.stop();  // Detener cualquier reproducción anterior
        mediaPlayer.play();  // Reproducir el sonido
    }
}
