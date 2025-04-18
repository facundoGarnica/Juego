package carne.Clases;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

public class Personaje {
    private ImageView sprite;  // El ImageView que representará al personaje
    private AnchorPane root;
    
    private boolean izquierdaPresionada = false;
    private boolean derechaPresionada = false;
    private boolean saltando = false;
    
    private double velocidadY = 0;
    private double velocidadX = 3.0;
    private final double GRAVEDAD = 0.3;
    private final double FUERZA_SALTO = -10;
    private final double Y_SUELO = 527.0;
    
    private boolean mirandoADerecha = true;  // Dirección de la imagen
    
    private Image imgCaminarIzquierda1, imgCaminarIzquierda2;
    private Image imgCaminarDerecha1, imgCaminarDerecha2;
    private Image imgIdleIzquierda, imgIdleDerecha;
    private Image imgtransicionIzquierda, imgtransicionDerecha;
    
    private long ultimoCambioFrame = 0;
    private int estadoAnimacion = 0;
    private final long INTERVALO_CAMBIO_FRAME = 250_000_000;

    // Modificado para recibir el ImageView
    public Personaje(AnchorPane root, ImageView sprite) {
        this.root = root;
        this.sprite = sprite;  // Usamos el ImageView pasado desde EntradaController
        System.out.println("Personaje creado con ImageView: " + sprite);
        inicializarImagenes();
        configurarPersonaje();
    }
    
    private void inicializarImagenes() {
        imgCaminarIzquierda1 = new Image(getClass().getResource("/Imagenes/Chica/der paso1.png").toExternalForm());
        imgCaminarIzquierda2 = new Image(getClass().getResource("/Imagenes/Chica/der paso2.png").toExternalForm());
        imgtransicionIzquierda = new Image(getClass().getResource("/Imagenes/Chica/der transicion.png").toExternalForm());
        imgCaminarDerecha1 = new Image(getClass().getResource("/Imagenes/Chica/izq paso1.png").toExternalForm());
        imgCaminarDerecha2 = new Image(getClass().getResource("/Imagenes/Chica/izq paso2.png").toExternalForm());
        imgtransicionDerecha = new Image(getClass().getResource("/Imagenes/Chica/izq transicion.png").toExternalForm());
        imgIdleIzquierda = new Image(getClass().getResource("/Imagenes/Chica/der idle.png").toExternalForm());
        imgIdleDerecha = new Image(getClass().getResource("/Imagenes/Chica/izq idle.png").toExternalForm());
    }

    private void configurarPersonaje() {
        sprite.setLayoutX(100);
        sprite.setLayoutY(Y_SUELO);
        sprite.setImage(imgIdleDerecha);
        System.out.println("Posición inicial del personaje: " + sprite.getLayoutX() + ", " + sprite.getLayoutY());
    }

    public void presionarTecla(KeyCode code) {
        if (null != code) {
            System.out.println("Tecla presionada: " + code);
            switch (code) {
                case LEFT:
                    izquierdaPresionada = true;
                    mirandoADerecha = false;  // Cambiar la dirección a izquierda
                    break;
                case RIGHT:
                    derechaPresionada = true;
                    mirandoADerecha = true;  // Cambiar la dirección a derecha
                    break;
                case SPACE:
                    if (!saltando) {
                        saltando = true;
                        velocidadY = FUERZA_SALTO;
                        System.out.println("Iniciando salto");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public void soltarTecla(KeyCode code) {
        if (code == KeyCode.LEFT) {
            izquierdaPresionada = false;
        } else if (code == KeyCode.RIGHT) {
            derechaPresionada = false;
        }
        System.out.println("Tecla soltada: " + code);
    }

    public void actualizar(long now) {
        actualizarMovimientoLateral(now);
        actualizarSaltoYGravedad(now);
        actualizarAnimacion(now);
    }

    private void actualizarMovimientoLateral(long now) {
        double nuevaX = sprite.getLayoutX();

        if (izquierdaPresionada) {
            nuevaX -= velocidadX;
            if (nuevaX >= 0) {
                sprite.setLayoutX(nuevaX);
                System.out.println("Moviendo a la izquierda, nueva X: " + nuevaX);
                cambiarImagenCaminarIzquierda(now);
            }
        } else if (derechaPresionada) {
            nuevaX += velocidadX;
            if (nuevaX + sprite.getBoundsInParent().getWidth() <= root.getWidth()) {
                sprite.setLayoutX(nuevaX);
                System.out.println("Moviendo a la derecha, nueva X: " + nuevaX);
                cambiarImagenCaminarDerecha(now);
            }
        } else if (!saltando) {
            if (mirandoADerecha) {
                sprite.setImage(imgIdleDerecha);
            } else {
                sprite.setImage(imgIdleIzquierda);
            }
        }
    }

    private void actualizarSaltoYGravedad(long now) {
        if (saltando) {
            velocidadY += GRAVEDAD;
            sprite.setLayoutY(sprite.getLayoutY() + velocidadY);

            if (sprite.getLayoutY() >= Y_SUELO) {
                sprite.setLayoutY(Y_SUELO);
                saltando = false;
                velocidadY = 0;
                if (izquierdaPresionada) {
                    sprite.setImage(imgIdleIzquierda);
                } else if (derechaPresionada) {
                    sprite.setImage(imgIdleDerecha);
                }
                System.out.println("Salto terminado, posición Y: " + sprite.getLayoutY());
            }
        }
    }

    private void actualizarAnimacion(long now) {
        if (saltando) {
            if (izquierdaPresionada) {
                cambiarImagenCaminarIzquierda(now);
            } else if (derechaPresionada) {
                cambiarImagenCaminarDerecha(now);
            } else {
                sprite.setImage(mirandoADerecha ? imgtransicionDerecha : imgtransicionIzquierda);
            }
        } else if (izquierdaPresionada) {
            cambiarImagenCaminarIzquierda(now);
        } else if (derechaPresionada) {
            cambiarImagenCaminarDerecha(now);
        }
    }

    private void cambiarImagenCaminarIzquierda(long now) {
        if (now - ultimoCambioFrame > INTERVALO_CAMBIO_FRAME) {
            switch (estadoAnimacion) {
                case 0:
                    sprite.setImage(imgCaminarIzquierda1);
                    break;
                case 1:
                    sprite.setImage(imgtransicionIzquierda);
                    break;
                case 2:
                    sprite.setImage(imgCaminarIzquierda2);
                    break;
                case 3:
                    sprite.setImage(imgtransicionIzquierda);
                    break;
            }
            estadoAnimacion = (estadoAnimacion + 1) % 4;
            ultimoCambioFrame = now;
            System.out.println("Cambiando imagen caminar izquierda, estado animación: " + estadoAnimacion);
        }
    }

    private void cambiarImagenCaminarDerecha(long now) {
        if (now - ultimoCambioFrame > INTERVALO_CAMBIO_FRAME) {
            switch (estadoAnimacion) {
                case 0:
                    sprite.setImage(imgCaminarDerecha1);
                    break;
                case 1:
                    sprite.setImage(imgtransicionDerecha);
                    break;
                case 2:
                    sprite.setImage(imgCaminarDerecha2);
                    break;
                case 3:
                    sprite.setImage(imgtransicionDerecha);
                    break;
            }
            estadoAnimacion = (estadoAnimacion + 1) % 4;
            ultimoCambioFrame = now;
            System.out.println("Cambiando imagen caminar derecha, estado animación: " + estadoAnimacion);
        }
    }

    public ImageView getSprite() {
        return sprite;
    }
}
