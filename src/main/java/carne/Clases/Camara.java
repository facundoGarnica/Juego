package carne.Clases;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class Camara {

    private Personaje personaje1;
    private Personaje personaje2;
    private AnchorPane root;
    private Pane pared1;
    private Pane pared2;

    private final double VELOCIDAD_CAMARA = 3.0;

    public Camara(AnchorPane root, Personaje p1, Personaje p2, Pane pared1, Pane pared2) {
        this.root = root;
        this.personaje1 = p1;
        this.personaje2 = p2;
        this.pared1 = pared1;
        this.pared2 = pared2;
    }

    public void actualizar() {
        // Verificamos si los personajes están tocando las paredes
        boolean personaje1TocaPared1 = estaTocandoPared(personaje1, pared1);
        boolean personaje1TocaPared2 = estaTocandoPared(personaje1, pared2);
        boolean personaje2TocaPared1 = estaTocandoPared(personaje2, pared1);
        boolean personaje2TocaPared2 = estaTocandoPared(personaje2, pared2);

        // Si ambos personajes están tocando paredes opuestas, detenemos la cámara y a los personajes
        if ((personaje1TocaPared1 && personaje2TocaPared2) || (personaje1TocaPared2 && personaje2TocaPared1)) {
            detenerMovimiento();
            return;
        }

        // Control de detención para personaje1
        if (personaje1TocaPared1) {
            personaje1.setTrueIzquierdaDetenida();
        } else {
            personaje1.setFalseIzquierdaDetenida();
        }

        if (personaje1TocaPared2) {
            personaje1.setTrueDerechaDetenida();
        } else {
            personaje1.setFalseDerechaDetenida();
        }

        // Control de detención para personaje2
        if (personaje2TocaPared1) {
            personaje2.setTrueIzquierdaDetenida();
        } else {
            personaje2.setFalseIzquierdaDetenida();
        }

        if (personaje2TocaPared2) {
            personaje2.setTrueDerechaDetenida();
        } else {
            personaje2.setFalseDerechaDetenida();
        }

        // Si alguno toca solo pared1, movemos la cámara a la derecha
        if ((personaje1TocaPared1 || personaje2TocaPared1) && !personaje1TocaPared2 && !personaje2TocaPared2) {
            root.setTranslateX(root.getTranslateX() + VELOCIDAD_CAMARA);
        }

        // Si alguno toca solo pared2, movemos la cámara a la izquierda
        if ((personaje1TocaPared2 || personaje2TocaPared2) && !personaje1TocaPared1 && !personaje2TocaPared1) {
            root.setTranslateX(root.getTranslateX() - VELOCIDAD_CAMARA);
        }
    }

    // Detener movimiento de los personajes y cámara
    private void detenerMovimiento() {
        personaje1.detenerMovimiento();
        personaje2.detenerMovimiento();
    }

    private boolean estaTocandoPared(Personaje personaje, Pane pared) {
        double personajeX = personaje.getContenedor().getLayoutX();
        double personajeAncho = personaje.getContenedor().getWidth();
        double personajeDerecha = personajeX + personajeAncho;

        double paredX = pared.getLayoutX() - root.getTranslateX();
        double paredAncho = pared.getWidth();

        // Verifica si el personaje toca la pared
        return personajeDerecha >= paredX && personajeX <= paredX + paredAncho;
    }
}
