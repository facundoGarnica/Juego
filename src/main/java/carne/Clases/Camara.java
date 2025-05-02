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

        // Si ambos personajes están tocando paredes opuestas (pared1 y pared2), la cámara se detiene
        if ((personaje1TocaPared1 && personaje2TocaPared2) || (personaje1TocaPared2 && personaje2TocaPared1)) {
            return; // No movemos la cámara
        }

        // Si el personaje1 toca la pared1 (izquierda), movemos la cámara a la derecha
        if (personaje1TocaPared1 && !personaje2TocaPared2) {
            root.setTranslateX(root.getTranslateX() + VELOCIDAD_CAMARA);
        }

        // Si el personaje1 toca la pared2 (derecha), movemos la cámara a la izquierda
        if (personaje1TocaPared2 && !personaje2TocaPared1) {
            root.setTranslateX(root.getTranslateX() - VELOCIDAD_CAMARA);
        }

        // Si el personaje2 toca la pared1 (izquierda), movemos la cámara a la derecha
        if (personaje2TocaPared1 && !personaje1TocaPared2) {
            root.setTranslateX(root.getTranslateX() + VELOCIDAD_CAMARA);
        }

        // Si el personaje2 toca la pared2 (derecha), movemos la cámara a la izquierda
        if (personaje2TocaPared2 && !personaje1TocaPared1) {
            root.setTranslateX(root.getTranslateX() - VELOCIDAD_CAMARA);
        }
    }

    // Método para detectar si un personaje toca una pared
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
