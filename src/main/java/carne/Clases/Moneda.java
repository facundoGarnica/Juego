package carne.Clases;

import javafx.animation.TranslateTransition;
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
    private double distancia = 15.0;
    private TranslateTransition transition;
    private MediaPlayer mediaPlayer;

    public Moneda(Pane monedaPane, Personaje personaje1, Personaje personaje2, Pane root) {
        this.monedaPane = monedaPane;
        this.personaje1 = personaje1;
        this.personaje2 = personaje2;
        this.root = root;

        // Cargar sonido
        String path = "sound/Monedas/recolectar.mp3";
        Media media = new Media(getClass().getClassLoader().getResource(path).toString());
        mediaPlayer = new MediaPlayer(media);

        // Animación de flotación
        Random random = new Random();
        double duracion = 1000 + random.nextInt(1000);

        transition = new TranslateTransition(Duration.millis(duracion), monedaPane);
        transition.setByY(-distancia);
        transition.setAutoReverse(true);
        transition.setCycleCount(TranslateTransition.INDEFINITE);
        transition.setInterpolator(javafx.animation.Interpolator.EASE_BOTH);
        transition.play();
    }

    public void actualizar() {
        if (!root.getChildren().contains(monedaPane)) return;

        if (esColision(personaje1)) {
            personaje1.agregarPuntos(20);
            personaje1.agregarMoneda();  // ← Aumentar contador de monedas
            root.getChildren().remove(monedaPane);
            System.out.println("Moneda agarrada por jugador 1 - Total monedas: " + personaje1.getMonedas());
            playSound();
            transition.stop();
        } else if (esColision(personaje2)) {
            personaje2.agregarPuntos(20);
            personaje2.agregarMoneda();  // ← Aumentar contador de monedas
            root.getChildren().remove(monedaPane);
            System.out.println("Moneda agarrada por jugador 2 - Total monedas: " + personaje2.getMonedas());
            playSound();
            transition.stop();
        }
    }

    private boolean esColision(Personaje p) {
        return monedaPane.getBoundsInParent().intersects(p.getContenedor().getBoundsInParent());
    }

    private void playSound() {
        mediaPlayer.stop();
        mediaPlayer.play();
    }
}
