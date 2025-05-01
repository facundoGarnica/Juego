package carne.Clases;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class Camara {

    private Personaje personaje1;
    private Personaje personaje2;
    private AnchorPane root;
    private Pane pared1;
    private Pane pared2;

    private double desplazamientoActual = 0;
    private final double suavizadoBase = 0.1;
    private final long INTERVALO_CAMBIO_FRAME = 100_000_000;

    private final double ANCHO_PANTALLA = 800;

    private boolean movimientoActivo = true;

    public Camara(AnchorPane root, Personaje p1, Personaje p2, Pane pared1, Pane pared2) {
        this.root = root;
        this.personaje1 = p1;
        this.personaje2 = p2;
        this.pared1 = pared1;
        this.pared2 = pared2;
    }

    public void actualizar() {
        // Verificamos si alguno de los personajes está tocando una pared
        boolean personaje1TocaPared = estaTocandoPared(personaje1);
        boolean personaje2TocaPared = estaTocandoPared(personaje2);

        // Si ambos personajes están tocando alguna pared, se detienen
        if (personaje1TocaPared) {
            personaje1.detenerMovimiento(); // Detenemos el movimiento de personaje1
        }
        if (personaje2TocaPared) {
            personaje2.detenerMovimiento(); // Detenemos el movimiento de personaje2
        }

        // Ahora, se actualiza la cámara solo si los personajes no tocan las paredes
        if (!personaje1TocaPared && !personaje2TocaPared) {
            double centro1 = personaje1.getContenedor().getLayoutX() + personaje1.getContenedor().getWidth() / 2;
            double centro2 = personaje2.getContenedor().getLayoutX() + personaje2.getContenedor().getWidth() / 2;

            double centroPromedio = (centro1 + centro2) / 2;
            double objetivo = -(centroPromedio - ANCHO_PANTALLA / 2);

            double tiempoEnSegundos = INTERVALO_CAMBIO_FRAME / 1_000_000_000.0;
            double suavizado = suavizadoBase * tiempoEnSegundos;

            desplazamientoActual += (objetivo - desplazamientoActual) * suavizado;
            root.setTranslateX(desplazamientoActual);
        }
    }

    private boolean estaTocandoPared(Personaje personaje) {
        double personajeX = personaje.getContenedor().getLayoutX();
        double personajeAncho = personaje.getContenedor().getWidth();
        double personajeDerecha = personajeX + personajeAncho;

        double pared1X = pared1.getLayoutX() - root.getTranslateX();
        double pared2X = pared2.getLayoutX() - root.getTranslateX();

        // Verificamos si el personaje toca alguna de las paredes
        boolean tocaPared1 = personajeDerecha >= pared1X && personajeX <= pared1X + pared1.getWidth();
        boolean tocaPared2 = personajeX <= pared2X + pared2.getWidth() && personajeDerecha >= pared2X;

        return tocaPared1 || tocaPared2;
    }
}
