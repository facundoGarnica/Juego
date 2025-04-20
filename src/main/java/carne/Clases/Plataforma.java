package carne.Clases;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class Plataforma {
    private Pane plataforma;

    public Plataforma(Pane plataforma) {
        this.plataforma = plataforma;
    }

    public Bounds getBounds() {
        return plataforma.getBoundsInParent();
    }

    // Detectar colisión desde arriba
    public boolean detectarColisionDesdeArriba(Node personaje, double velocidadY) {
        Bounds pjBounds = personaje.getBoundsInParent();
        Bounds plataformaBounds = getBounds();

        boolean intersecta = pjBounds.intersects(plataformaBounds);
        boolean caeDesdeArriba = velocidadY > 0
                && pjBounds.getMaxY() <= plataformaBounds.getMinY() + 10
                && pjBounds.getMaxX() > plataformaBounds.getMinX() + 10
                && pjBounds.getMinX() < plataformaBounds.getMaxX() - 10;

        if (intersecta && caeDesdeArriba) {
            personaje.setLayoutY(plataformaBounds.getMinY() - pjBounds.getHeight());
            return true;
        }
        return false;
    }

    // Detectar colisión desde abajo
    public boolean detectarColisionDesdeAbajo(Node personaje, double velocidadY) {
        Bounds pjBounds = personaje.getBoundsInParent();
        Bounds plataformaBounds = getBounds();

        boolean intersecta = pjBounds.intersects(plataformaBounds);
        boolean chocaDesdeAbajo = velocidadY < 0
                && pjBounds.getMinY() >= plataformaBounds.getMaxY() - 10
                && pjBounds.getMaxX() > plataformaBounds.getMinX() + 10
                && pjBounds.getMinX() < plataformaBounds.getMaxX() - 10;

        if (intersecta && chocaDesdeAbajo) {
            personaje.setLayoutY(plataformaBounds.getMaxY());
            return true;
        }
        return false;
    }

    // Detectar colisión desde la izquierda
    public boolean detectarColisionLateralIzquierda(Node personaje, double velocidadX) {
        Bounds pjBounds = personaje.getBoundsInParent();
        Bounds plataformaBounds = getBounds();

        boolean intersecta = pjBounds.intersects(plataformaBounds);
        boolean chocaIzq = velocidadX > 0
                && pjBounds.getMaxX() >= plataformaBounds.getMinX()
                && pjBounds.getMinX() < plataformaBounds.getMinX()
                && pjBounds.getMaxY() > plataformaBounds.getMinY() + 10;

        if (intersecta && chocaIzq) {
            personaje.setLayoutX(plataformaBounds.getMinX() - pjBounds.getWidth());
            return true;
        }
        return false;
    }

    // Detectar colisión desde la derecha
    public boolean detectarColisionLateralDerecha(Node personaje, double velocidadX) {
        Bounds pjBounds = personaje.getBoundsInParent();
        Bounds plataformaBounds = getBounds();

        boolean intersecta = pjBounds.intersects(plataformaBounds);
        boolean chocaDer = velocidadX < 0
                && pjBounds.getMinX() < plataformaBounds.getMaxX()
                && pjBounds.getMaxX() > plataformaBounds.getMaxX() - 5
                && pjBounds.getMaxY() > plataformaBounds.getMinY() + 5
                && pjBounds.getMinY() < plataformaBounds.getMaxY() - 5;

        if (intersecta && chocaDer) {
            personaje.setLayoutX(plataformaBounds.getMaxX());
            return true;
        }
        return false;
    }
}
