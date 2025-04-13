package carne.Ventanas;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

public class EntradaController implements Initializable {

    // Variables de movimiento
    private boolean izquierdaPresionada = false;
    private boolean derechaPresionada = false;

    // Variables para el salto
    private boolean saltando = false;
    private double velocidadY = 0;
    private final double GRAVEDAD = 0.5;
    private final double FUERZA_SALTO = -15;
    private final double Y_SUELO = 527.0;

    // Cargar imágenes de los fotogramas
    private Image imgCaminarIzquierda1 = new Image(getClass().getResource("/Imagenes/Personaje/Izquierda1.png").toExternalForm());
    private Image imgCaminarIzquierda2 = new Image(getClass().getResource("/Imagenes/Personaje/Izquierda2.png").toExternalForm());
    private Image imgCaminarDerecha1 = new Image(getClass().getResource("/Imagenes/Personaje/Derecha1.png").toExternalForm());
    private Image imgCaminarDerecha2 = new Image(getClass().getResource("/Imagenes/Personaje/Derecha2.png").toExternalForm());
    private Image imgIdleIzquierda = new Image(getClass().getResource("/Imagenes/Personaje/IdleIzq.png").toExternalForm());
    private Image imgIdleDerecha = new Image(getClass().getResource("/Imagenes/Personaje/IdleDer.png").toExternalForm());

    // Variables de temporizador
    private long ultimoCambioFrame = 0;
    private boolean frameAlterno = false;
    private final long INTERVALO_CAMBIO_FRAME = 200_000_000; // 200ms en nanosegundos

    @FXML
    private AnchorPane rootPane;
    @FXML
    private ImageView ImagenPJ;

    // Variables para animación de caminata
    private boolean caminandoIzquierda = false;
    private boolean caminandoDerecha = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rootPane.setFocusTraversable(true);
        rootPane.requestFocus();

        // Eventos de teclado
        rootPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                izquierdaPresionada = true;
                caminandoIzquierda = true;
            }
            if (event.getCode() == KeyCode.RIGHT) {
                derechaPresionada = true;
                caminandoDerecha = true;
            }
            if (event.getCode() == KeyCode.SPACE) {
                if (!saltando) {
                    saltando = true;
                    velocidadY = FUERZA_SALTO;
                }
            }
        });

        rootPane.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                izquierdaPresionada = false;
                caminandoIzquierda = false;
                ImagenPJ.setImage(imgIdleIzquierda);
            }
            if (event.getCode() == KeyCode.RIGHT) {
                derechaPresionada = false;
                caminandoDerecha = false;
                ImagenPJ.setImage(imgIdleDerecha);
            }
        });

        // Loop de animación
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double velocidadX = 5;

                // Temporizador para animación
                if (now - ultimoCambioFrame > INTERVALO_CAMBIO_FRAME) {
                    frameAlterno = !frameAlterno;
                    ultimoCambioFrame = now;
                }

                // Movimiento lateral y cambio de imagen
                if (izquierdaPresionada) {
                    ImagenPJ.setLayoutX(ImagenPJ.getLayoutX() - velocidadX);
                    cambiarImagenCaminarIzquierda(frameAlterno);
                } else if (derechaPresionada) {
                    ImagenPJ.setLayoutX(ImagenPJ.getLayoutX() + velocidadX);
                    cambiarImagenCaminarDerecha(frameAlterno);
                } else {
                    // Si no está moviendo, poner en estado idle
                    if (caminandoIzquierda) {
                        ImagenPJ.setImage(imgIdleIzquierda);
                    } else if (caminandoDerecha) {
                        ImagenPJ.setImage(imgIdleDerecha);
                    }
                }

                // Salto y gravedad
                if (saltando) {
                    velocidadY += GRAVEDAD;
                    ImagenPJ.setLayoutY(ImagenPJ.getLayoutY() + velocidadY);

                    if (ImagenPJ.getLayoutY() >= Y_SUELO) {
                        ImagenPJ.setLayoutY(Y_SUELO);
                        saltando = false;
                        velocidadY = 0;
                        if (caminandoIzquierda) {
                            ImagenPJ.setImage(imgIdleIzquierda);
                        } else if (caminandoDerecha) {
                            ImagenPJ.setImage(imgIdleDerecha);
                        }
                    }
                }
            }
        };
        gameLoop.start();
    }

    // Cambiar a las imágenes de caminar hacia la izquierda con animación temporizada
    private void cambiarImagenCaminarIzquierda(boolean alterno) {
        ImagenPJ.setImage(alterno ? imgCaminarIzquierda1 : imgCaminarIzquierda2);
    }

    // Cambiar a las imágenes de caminar hacia la derecha con animación temporizada
    private void cambiarImagenCaminarDerecha(boolean alterno) {
        ImagenPJ.setImage(alterno ? imgCaminarDerecha1 : imgCaminarDerecha2);
    }
}
