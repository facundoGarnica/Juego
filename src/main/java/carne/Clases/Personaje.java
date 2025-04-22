package carne.Clases;

import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import java.util.List;

public class Personaje {
    private Pane contenedor;
    private ImageView sprite;
    private AnchorPane root;
    private List<Plataforma> plataformas;

    private boolean izquierdaPresionada = false;
    private boolean derechaPresionada = false;
    private boolean saltando = false;

    private double velocidadY = 0;
    private double velocidadX = 3.0;
    private final double GRAVEDAD = 0.3;
    private final double FUERZA_SALTO = -10;
    private final double Y_SUELO = 527.0;

    private boolean mirandoADerecha = true;

    private Image imgCaminarIzquierda1, imgCaminarIzquierda2;
    private Image imgCaminarDerecha1, imgCaminarDerecha2;
    private Image imgIdleIzquierda, imgIdleDerecha;
    private Image imgtransicionIzquierda, imgtransicionDerecha;

    private long ultimoCambioFrame = 0;
    private int estadoAnimacion = 0;
    private final long INTERVALO_CAMBIO_FRAME = 100_000_000;

    public Personaje(AnchorPane root, Pane contenedor, ImageView sprite, Image imgCaminarIzquierda1, Image imgCaminarIzquierda2,
                     Image imgCaminarDerecha1, Image imgCaminarDerecha2, Image imgIdleIzquierda,
                     Image imgIdleDerecha, Image imgtransicionIzquierda, Image imgtransicionDerecha) {
        this.root = root;
        this.contenedor = contenedor;
        this.sprite = sprite;

        this.imgCaminarIzquierda1 = imgCaminarIzquierda1;
        this.imgCaminarIzquierda2 = imgCaminarIzquierda2;
        this.imgCaminarDerecha1 = imgCaminarDerecha1;
        this.imgCaminarDerecha2 = imgCaminarDerecha2;
        this.imgIdleIzquierda = imgIdleIzquierda;
        this.imgIdleDerecha = imgIdleDerecha;
        this.imgtransicionIzquierda = imgtransicionIzquierda;
        this.imgtransicionDerecha = imgtransicionDerecha;

        this.plataformas = new ArrayList<>();
        configurarPersonaje();
    }

    public void setPlataformas(List<Plataforma> plataformas) {
        this.plataformas = plataformas;
    }

    private void configurarPersonaje() {
        contenedor.setLayoutY(Y_SUELO);
        sprite.setImage(imgIdleDerecha);
    }

    public void presionarTecla(KeyCode code) {
        switch (code) {
            case LEFT -> {
                izquierdaPresionada = true;
                mirandoADerecha = false;
            }
            case RIGHT -> {
                derechaPresionada = true;
                mirandoADerecha = true;
            }
            case SPACE -> {
                if (!saltando) {
                    saltando = true;
                    velocidadY = FUERZA_SALTO;
                }
            }
        }
    }

    public void soltarTecla(KeyCode code) {
        if (code == KeyCode.LEFT) izquierdaPresionada = false;
        if (code == KeyCode.RIGHT) derechaPresionada = false;
    }

    public void actualizar(long now) {
        moverHorizontal(now);
        aplicarGravedad(now);
        animar(now);
    }

    private void moverHorizontal(long now) {
        double nuevaX = contenedor.getLayoutX();
        boolean colision = false;

        if (izquierdaPresionada) {
            nuevaX -= velocidadX;

            for (Plataforma p : plataformas) {
                if (p.detectarColisionLateralDerecha(contenedor, -velocidadX)) {
                    colision = true;
                    break;
                }
            }

            if (!colision && nuevaX >= 0) {
                contenedor.setLayoutX(nuevaX);
                cambiarImagenCaminarIzquierda(now);
            }
        } else if (derechaPresionada) {
            nuevaX += velocidadX;

            for (Plataforma p : plataformas) {
                if (p.detectarColisionLateralIzquierda(contenedor, velocidadX)) {
                    colision = true;
                    break;
                }
            }

            if (!colision && nuevaX + contenedor.getWidth() <= root.getWidth()) {
                contenedor.setLayoutX(nuevaX);
                cambiarImagenCaminarDerecha(now);
            }
        } else if (!saltando) {
            sprite.setImage(mirandoADerecha ? imgIdleDerecha : imgIdleIzquierda);
        }
    }

    public void aplicarGravedad(long now) {
    velocidadY += GRAVEDAD;
    contenedor.setLayoutY(contenedor.getLayoutY() + velocidadY);

    boolean enPlataforma = false;

    for (Plataforma p : plataformas) {
        // Si el personaje cae sobre la plataforma
        if (p.detectarColisionDesdeArriba(contenedor, velocidadY)) {
            System.out.println("Toco arriba");
            enPlataforma = true;
            velocidadY = 0;
            saltando = false;
            contenedor.setLayoutY(p.getBounds().getMinY() - contenedor.getHeight()); // Justo encima
            break;
        }

        // Si el personaje choca desde abajo (al saltar)
        if (p.detectarColisionDesdeAbajo(contenedor, velocidadY)) {
            System.out.println("Toco abajo");
            velocidadY = 0;
            break;
        }
    }

    // Si llega al suelo
    if (contenedor.getLayoutY() >= Y_SUELO) {
        contenedor.setLayoutY(Y_SUELO);
        velocidadY = 0;
        saltando = false;
    }

    // Si no está en el suelo ni en plataforma, sigue en el aire
    if (!enPlataforma && contenedor.getLayoutY() < Y_SUELO) {
        saltando = true;
    }
}








    private void animar(long now) {
    if (saltando) {
        // Cambiar el sprite del salto a la imagen de caminar dependiendo de la dirección
        sprite.setImage(mirandoADerecha ? imgCaminarDerecha1 : imgCaminarIzquierda1);
    } else if (izquierdaPresionada) {
        cambiarImagenCaminarIzquierda(now);
    } else if (derechaPresionada) {
        cambiarImagenCaminarDerecha(now);
    }
}


    private void cambiarImagenCaminarIzquierda(long now) {
        if (now - ultimoCambioFrame > INTERVALO_CAMBIO_FRAME) {
            switch (estadoAnimacion) {
                case 0 -> sprite.setImage(imgCaminarIzquierda1);
                case 1 -> sprite.setImage(imgtransicionIzquierda);
                case 2 -> sprite.setImage(imgCaminarIzquierda2);
                case 3 -> sprite.setImage(imgtransicionIzquierda);
            }
            estadoAnimacion = (estadoAnimacion + 1) % 4;
            ultimoCambioFrame = now;
        }
    }

    private void cambiarImagenCaminarDerecha(long now) {
        if (now - ultimoCambioFrame > INTERVALO_CAMBIO_FRAME) {
            switch (estadoAnimacion) {
                case 0 -> sprite.setImage(imgCaminarDerecha1);
                case 1 -> sprite.setImage(imgtransicionDerecha);
                case 2 -> sprite.setImage(imgCaminarDerecha2);
                case 3 -> sprite.setImage(imgtransicionDerecha);
            }
            estadoAnimacion = (estadoAnimacion + 1) % 4;
            ultimoCambioFrame = now;
        }
    }

    public Pane getContenedor() {
        return contenedor;
    }

    public ImageView getSprite() {
        return sprite;
    }
}