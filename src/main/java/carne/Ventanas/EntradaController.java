package carne.Ventanas;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class EntradaController implements Initializable {

    private boolean izquierdaPresionada = false;
    private boolean derechaPresionada = false;
    private boolean saltando = false;

    private double velocidadY = 0;
    private final double GRAVEDAD = 0.3;
    private final double FUERZA_SALTO = -10;
    private final double Y_SUELO = 527.0;

    private Image imgCaminarIzquierda1, imgCaminarIzquierda2;
    private Image imgCaminarDerecha1, imgCaminarDerecha2;
    private Image imgIdleIzquierda, imgIdleDerecha;

    private long ultimoCambioFrame = 0;
    private boolean frameAlterno = false;
    private final long INTERVALO_CAMBIO_FRAME = 200_000_000;

    @FXML
    private AnchorPane rootPane;
    @FXML
    private ImageView ImagenPJ;
    @FXML
    private Pane Plataforma;
    @FXML
    private Pane Plataforma1;
    @FXML
    private Pane Plataforma2;

    private boolean caminandoIzquierda = false;
    private boolean caminandoDerecha = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        inicializarImagenes();
        configurarPersonaje();
        configurarEventosTeclado();
        iniciarBucleJuego();
    }

    private void inicializarImagenes() {
        imgCaminarIzquierda1 = new Image(getClass().getResource("/Imagenes/Personaje/Izquierda1.png").toExternalForm());
        imgCaminarIzquierda2 = new Image(getClass().getResource("/Imagenes/Personaje/Izquierda2.png").toExternalForm());
        imgCaminarDerecha1 = new Image(getClass().getResource("/Imagenes/Personaje/Derecha1.png").toExternalForm());
        imgCaminarDerecha2 = new Image(getClass().getResource("/Imagenes/Personaje/Derecha2.png").toExternalForm());
        imgIdleIzquierda = new Image(getClass().getResource("/Imagenes/Personaje/IdleIzq.png").toExternalForm());
        imgIdleDerecha = new Image(getClass().getResource("/Imagenes/Personaje/IdleDer.png").toExternalForm());
    }

    private void configurarPersonaje() {
        rootPane.setFocusTraversable(true);
        rootPane.requestFocus();

        // Posición inicial y estado visible
        ImagenPJ.setLayoutX(100); // o la posición X que desees
        ImagenPJ.setLayoutY(Y_SUELO);
        ImagenPJ.setImage(imgIdleDerecha);
    }

    private void configurarEventosTeclado() {
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
                if (!saltando) {
                    ImagenPJ.setImage(imgIdleIzquierda);
                }
            }
            if (event.getCode() == KeyCode.RIGHT) {
                derechaPresionada = false;
                caminandoDerecha = false;
                if (!saltando) {
                    ImagenPJ.setImage(imgIdleDerecha);
                }
            }
        });
    }

    private void iniciarBucleJuego() {
        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                actualizarMovimientoLateral(now);
                actualizarSaltoYGravedad(now);
                detectarColisionConPlataforma();
            }
        };
        gameLoop.start();
    }

    private void actualizarMovimientoLateral(long now) {
        double velocidadX = 5;
        double nuevaX = ImagenPJ.getLayoutX();

        if (izquierdaPresionada) {
            nuevaX -= velocidadX;
            if (nuevaX >= 0) {
                ImagenPJ.setLayoutX(nuevaX);
                cambiarImagenCaminarIzquierda(now);
            }
        } else if (derechaPresionada) {
            nuevaX += velocidadX;
            if (nuevaX + ImagenPJ.getBoundsInParent().getWidth() <= rootPane.getWidth()) {
                ImagenPJ.setLayoutX(nuevaX);
                cambiarImagenCaminarDerecha(now);
            }
        } else if (!saltando) {
            if (caminandoIzquierda) {
                ImagenPJ.setImage(imgIdleIzquierda);
            } else if (caminandoDerecha) {
                ImagenPJ.setImage(imgIdleDerecha);
            }
        }
    }

    private void actualizarSaltoYGravedad(long now) {
        if (saltando) {
            velocidadY += GRAVEDAD;
            ImagenPJ.setLayoutY(ImagenPJ.getLayoutY() + velocidadY);

            if (izquierdaPresionada || caminandoIzquierda) {
                cambiarImagenCaminarIzquierda(now);
            } else if (derechaPresionada || caminandoDerecha) {
                cambiarImagenCaminarDerecha(now);
            }

            if (ImagenPJ.getLayoutY() >= Y_SUELO) {
                ImagenPJ.setLayoutY(Y_SUELO);
                saltando = false;
                velocidadY = 0;

                if (caminandoIzquierda) {
                    ImagenPJ.setImage(imgIdleIzquierda);
                } else if (caminandoDerecha) {
                    ImagenPJ.setImage(imgIdleDerecha);
                }

                crearParticulasAterrizaje();
            }
        }
    }

    private void cambiarImagenCaminarIzquierda(long now) {
        if (now - ultimoCambioFrame > INTERVALO_CAMBIO_FRAME) {
            ImagenPJ.setImage(frameAlterno ? imgCaminarIzquierda1 : imgCaminarIzquierda2);
            frameAlterno = !frameAlterno;
            ultimoCambioFrame = now;
        }
    }

    private void cambiarImagenCaminarDerecha(long now) {
        if (now - ultimoCambioFrame > INTERVALO_CAMBIO_FRAME) {
            ImagenPJ.setImage(frameAlterno ? imgCaminarDerecha1 : imgCaminarDerecha2);
            frameAlterno = !frameAlterno;
            ultimoCambioFrame = now;
        }
    }

    private void crearParticulasAterrizaje() {
        for (int i = 0; i < 5; i++) {
            Circle particula = new Circle(3, Color.BURLYWOOD);
            particula.setLayoutX(ImagenPJ.getLayoutX() + ImagenPJ.getBoundsInParent().getWidth() / 2 + Math.random() * 20 - 10);
            particula.setLayoutY(Y_SUELO + ImagenPJ.getBoundsInParent().getHeight() - 5 + Math.random() * 5);
            rootPane.getChildren().add(particula);

            FadeTransition fade = new FadeTransition(Duration.millis(400 + Math.random() * 200), particula);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setOnFinished(e -> rootPane.getChildren().remove(particula));
            fade.play();
        }
    }

    private void detectarColisionConPlataforma() {
        Bounds pjBounds = ImagenPJ.getBoundsInParent();
        Bounds plataformaBounds = Plataforma.getBoundsInParent();

        boolean intersecta = pjBounds.intersects(plataformaBounds);

        // Colisión desde arriba (plataformeo)
        boolean caeDesdeArriba = velocidadY > 0
                && pjBounds.getMaxY() <= plataformaBounds.getMinY() + 10
                && pjBounds.getMaxX() > plataformaBounds.getMinX() + 10
                && pjBounds.getMinX() < plataformaBounds.getMaxX() - 10;

        if (intersecta && caeDesdeArriba) {
            ImagenPJ.setLayoutY(plataformaBounds.getMinY() - pjBounds.getHeight());
            velocidadY = 0;
            saltando = false;
        }

        // Colisión lateral por la izquierda del personaje (bloquea la derecha de la plataforma)
        if (intersecta
                && pjBounds.getMinX() < plataformaBounds.getMaxX()
                && pjBounds.getMaxX() > plataformaBounds.getMaxX() - 10
                && pjBounds.getMaxY() > plataformaBounds.getMinY() + 10) {

            ImagenPJ.setLayoutX(plataformaBounds.getMaxX());
        }

        // Colisión lateral por la derecha del personaje (bloquea la izquierda de la plataforma)
        if (intersecta
                && pjBounds.getMaxX() > plataformaBounds.getMinX()
                && pjBounds.getMinX() < plataformaBounds.getMinX() + 10
                && pjBounds.getMaxY() > plataformaBounds.getMinY() + 10) {

            ImagenPJ.setLayoutX(plataformaBounds.getMinX() - pjBounds.getWidth());
        }

        // Colisión desde abajo (evita saltar a través de la plataforma)
        if (intersecta
                && pjBounds.getMinY() < plataformaBounds.getMaxY()
                && pjBounds.getMaxY() > plataformaBounds.getMaxY() - 10
                && velocidadY < 0) {

            // Rebota un poco o simplemente detiene la subida
            velocidadY = 0;
        }

        // Si el personaje ya no está sobre la plataforma
        if (!(pjBounds.getMaxY() <= plataformaBounds.getMinY() + 5
                && pjBounds.getMaxY() >= plataformaBounds.getMinY() - 10
                && pjBounds.getMaxX() > plataformaBounds.getMinX() + 10
                && pjBounds.getMinX() < plataformaBounds.getMaxX() - 10)) {

            // Si no está tocando suelo
            if (ImagenPJ.getLayoutY() < Y_SUELO && !saltando) {
                saltando = true;
            }
        }
    }
}
