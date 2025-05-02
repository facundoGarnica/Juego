package carne.Clases;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

public class Personaje {

    private boolean DerechaDetenida = false;
    private boolean IzquierdaDetenida = false;
    private int vida = 500;
    private final int VIDA_MAXIMA = 500;
    private int monedas = 0;
    private Pane contenedor;
    private ImageView sprite;
    private List<Plataforma> plataformas;
    private int puntos = 0;

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
    private Image imgTransicionIzquierda, imgTransicionDerecha;

    private long ultimoCambioFrame = 0;
    private int estadoAnimacion = 0;
    private final long INTERVALO_CAMBIO_FRAME = 100_000_000;

    public Personaje(Pane contenedor, ImageView sprite,
            Image imgCaminarIzquierda1, Image imgCaminarIzquierda2,
            Image imgCaminarDerecha1, Image imgCaminarDerecha2,
            Image imgIdleIzquierda, Image imgIdleDerecha,
            Image imgTransicionIzquierda, Image imgTransicionDerecha) {

        this.contenedor = contenedor;
        this.sprite = sprite;

        this.imgCaminarIzquierda1 = imgCaminarIzquierda1;
        this.imgCaminarIzquierda2 = imgCaminarIzquierda2;
        this.imgCaminarDerecha1 = imgCaminarDerecha1;
        this.imgCaminarDerecha2 = imgCaminarDerecha2;
        this.imgIdleIzquierda = imgIdleIzquierda;
        this.imgIdleDerecha = imgIdleDerecha;
        this.imgTransicionIzquierda = imgTransicionIzquierda;
        this.imgTransicionDerecha = imgTransicionDerecha;

        this.plataformas = new ArrayList<>();
        configurarPersonaje();
    }

    public void detenerMovimiento() {
        izquierdaPresionada = false;
        derechaPresionada = false;
    }

    private void configurarPersonaje() {
        contenedor.setLayoutY(Y_SUELO);
        sprite.setImage(imgIdleDerecha);
    }

    public void setPlataformas(List<Plataforma> plataformas) {
        this.plataformas = plataformas;
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
        if (code == KeyCode.LEFT) {
            izquierdaPresionada = false;
        }
        if (code == KeyCode.RIGHT) {
            derechaPresionada = false;
        }
    }

    public void actualizar(long now) {
        moverHorizontal(now);
        aplicarGravedad(now);
        animar(now);
    }

    private void moverHorizontal(long now) {
        double nuevaX = contenedor.getLayoutX();
        boolean colision = false;

        if (izquierdaPresionada && !IzquierdaDetenida) {
            double posibleX = nuevaX - velocidadX;

            for (Plataforma p : plataformas) {
                if (p.detectarColisionLateralDerecha(contenedor, -velocidadX)) {
                    colision = true;
                    break;
                }
            }

            if (posibleX < 0) {
                posibleX = 0;
                colision = true;
            }

            if (!colision) {
                contenedor.setLayoutX(posibleX);
                cambiarImagenCaminarIzquierda(now);
            } else {
                sprite.setImage(imgIdleIzquierda);
            }

        } else if (derechaPresionada && !DerechaDetenida) {
            double posibleX = nuevaX + velocidadX;

            for (Plataforma p : plataformas) {
                if (p.detectarColisionLateralIzquierda(contenedor, velocidadX)) {
                    colision = true;
                    break;
                }
            }

            double maxX = contenedor.getParent().getLayoutBounds().getMaxX() - contenedor.getWidth();
            if (posibleX > maxX) {
                posibleX = maxX;
                colision = true;
            }

            if (!colision) {
                contenedor.setLayoutX(posibleX);
                cambiarImagenCaminarDerecha(now);
            } else {
                sprite.setImage(imgIdleDerecha);
            }

        } else if (!saltando) {
            sprite.setImage(mirandoADerecha ? imgIdleDerecha : imgIdleIzquierda);
        }
    }

    private void aplicarGravedad(long now) {
        velocidadY += GRAVEDAD;
        contenedor.setLayoutY(contenedor.getLayoutY() + velocidadY);

        boolean enPlataforma = false;

        for (Plataforma p : plataformas) {
            if (p.detectarColisionDesdeArriba(contenedor, velocidadY)) {
                enPlataforma = true;
                velocidadY = 0;
                saltando = false;
                contenedor.setLayoutY(p.getBounds().getMinY() - contenedor.getHeight());
                break;
            }
            if (p.detectarColisionDesdeAbajo(contenedor, velocidadY)) {
                velocidadY = 0;
                break;
            }
        }

        if (contenedor.getLayoutY() >= Y_SUELO) {
            contenedor.setLayoutY(Y_SUELO);
            velocidadY = 0;
            saltando = false;
        }

        if (!enPlataforma && contenedor.getLayoutY() < Y_SUELO) {
            saltando = true;
        }
    }

    private void animar(long now) {
        if (saltando) {
            sprite.setImage(mirandoADerecha ? imgCaminarDerecha1 : imgCaminarIzquierda1);
        } else if (izquierdaPresionada && !IzquierdaDetenida) {
            cambiarImagenCaminarIzquierda(now);
        } else if (derechaPresionada && !DerechaDetenida) {
            cambiarImagenCaminarDerecha(now);
        }
    }

    private void cambiarImagenCaminarIzquierda(long now) {
        if (now - ultimoCambioFrame > INTERVALO_CAMBIO_FRAME) {
            switch (estadoAnimacion) {
                case 0 -> sprite.setImage(imgCaminarIzquierda1);
                case 1 -> sprite.setImage(imgTransicionIzquierda);
                case 2 -> sprite.setImage(imgCaminarIzquierda2);
                case 3 -> sprite.setImage(imgTransicionIzquierda);
            }
            estadoAnimacion = (estadoAnimacion + 1) % 4;
            ultimoCambioFrame = now;
        }
    }

    private void cambiarImagenCaminarDerecha(long now) {
        if (now - ultimoCambioFrame > INTERVALO_CAMBIO_FRAME) {
            switch (estadoAnimacion) {
                case 0 -> sprite.setImage(imgCaminarDerecha1);
                case 1 -> sprite.setImage(imgTransicionDerecha);
                case 2 -> sprite.setImage(imgCaminarDerecha2);
                case 3 -> sprite.setImage(imgTransicionDerecha);
            }
            estadoAnimacion = (estadoAnimacion + 1) % 4;
            ultimoCambioFrame = now;
        }
    }

    public Pane getContenedor() {
        return contenedor;
    }

    public void agregarPuntos(int cantidad) {
        puntos += cantidad;
        System.out.println("Puntos: " + puntos);
    }

    public int getPuntos() {
        return puntos;
    }

    public void agregarMoneda() {
        monedas++;
    }

    public int getMonedas() {
        return monedas;
    }

    public void QuitarVida(int danio) {
        vida -= danio;
    }

    public int getVida() {
        return vida;
    }

    public void curarVida(int cantidad) {
        vida += cantidad;
        if (vida > VIDA_MAXIMA) {
            vida = VIDA_MAXIMA;
        }
    }

    public void setTrueIzquierdaDetenida() {
        IzquierdaDetenida = true;
    }

    public void setFalseIzquierdaDetenida() {
        IzquierdaDetenida = false;
    }

    public void setTrueDerechaDetenida() {
        DerechaDetenida = true;
    }

    public void setFalseDerechaDetenida() {
        DerechaDetenida = false;
    }
}
