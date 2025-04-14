package carne.Ventanas;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Node;
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

    private boolean enPlataforma1 = false;
    private boolean enPlataforma2 = false;
    private boolean enPlataforma3 = false;
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
    private double velocidadX;

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
                detectarColisionConElementosPorNombre("Plataforma");
                detectarColisionConElementosPorNombre("Plataforma1");
                detectarColisionConElementosPorNombre("Plataforma2");
            }
        };
        gameLoop.start();
    }

    private void actualizarMovimientoLateral(long now) {
        velocidadX = 3.9;
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

    public void detectarColisionConElementosPorNombre(String nombre) {
        Bounds pjBounds = ImagenPJ.getBoundsInParent();
        boolean intersecta = false;

        // Detectar la plataforma
        Pane plataforma = null;
        switch (nombre) {
            case "Plataforma":
                plataforma = Plataforma;
                break;
            case "Plataforma1":
                plataforma = Plataforma1;
                break;
            case "Plataforma2":
                plataforma = Plataforma2;
                break;
        }

        if (plataforma != null) {
            Bounds plataformaBounds = plataforma.getBoundsInParent();
            intersecta = pjBounds.intersects(plataformaBounds);

            // Colisión desde arriba (plataformeo)
            boolean caeDesdeArriba = velocidadY > 0
                    && pjBounds.getMaxY() <= plataformaBounds.getMinY() + 10
                    && pjBounds.getMaxX() > plataformaBounds.getMinX() + 10
                    && pjBounds.getMinX() < plataformaBounds.getMaxX() - 10;

            if (intersecta && caeDesdeArriba) {
                ImagenPJ.setLayoutY(plataformaBounds.getMinY() - pjBounds.getHeight());
                velocidadY = 0;
                saltando = false;

                // Activar booleans
                switch (nombre) {
                    case "Plataforma":
                        enPlataforma1 = true;
                        break;
                    case "Plataforma1":
                        enPlataforma2 = true;
                        break;
                    case "Plataforma2":
                        enPlataforma3 = true;
                        break;
                }
            }

            // 🔽 Colisión desde abajo
            boolean chocaDesdeAbajo = velocidadY < 0
                    && pjBounds.getMinY() >= plataformaBounds.getMaxY() - 10
                    && pjBounds.getMaxX() > plataformaBounds.getMinX() + 10
                    && pjBounds.getMinX() < plataformaBounds.getMaxX() - 10;

            if (intersecta && chocaDesdeAbajo) {
                ImagenPJ.setLayoutY(plataformaBounds.getMaxY());
                velocidadY = 0; // o -velocidadY para rebote
            }

            // ⬅️ Colisión lateral izquierda
            boolean chocaPorLaIzquierda = velocidadX > 0
                    && pjBounds.getMaxX() >= plataformaBounds.getMinX()
                    && pjBounds.getMinX() < plataformaBounds.getMinX()
                    && pjBounds.getMaxY() > plataformaBounds.getMinY() + 10;

            if (intersecta && chocaPorLaIzquierda) {
                ImagenPJ.setLayoutX(plataformaBounds.getMinX() - pjBounds.getWidth());
                velocidadX = 0;
            }

            // ➡️ Colisión lateral derecha (entra desde la izquierda)
            boolean chocaPorLaDerecha = velocidadX < 0
                    && pjBounds.getMinX() < plataformaBounds.getMaxX()
                    && pjBounds.getMaxX() > plataformaBounds.getMaxX() - 5
                    && pjBounds.getMaxY() > plataformaBounds.getMinY() + 5
                    && pjBounds.getMinY() < plataformaBounds.getMaxY() - 5;

            if (intersecta && chocaPorLaDerecha) {
                ImagenPJ.setLayoutX(plataformaBounds.getMaxX());
                velocidadX = 0;
            }
        }

        // Validación de booleans
        if ((nombre.equals("Plataforma") && !enPlataforma1)
                || (nombre.equals("Plataforma1") && !enPlataforma2)
                || (nombre.equals("Plataforma2") && !enPlataforma3)) {
            return;
        }

        // Ya no está sobre la plataforma
        if (!(pjBounds.getMaxY() <= plataforma.getBoundsInParent().getMinY() + 5
                && pjBounds.getMaxY() >= plataforma.getBoundsInParent().getMinY() - 10
                && pjBounds.getMaxX() > plataforma.getBoundsInParent().getMinX() + 10
                && pjBounds.getMinX() < plataforma.getBoundsInParent().getMaxX() - 10)) {

            switch (nombre) {
                case "Plataforma":
                    enPlataforma1 = false;
                    break;
                case "Plataforma1":
                    enPlataforma2 = false;
                    break;
                case "Plataforma2":
                    enPlataforma3 = false;
                    break;
            }

            if (ImagenPJ.getLayoutY() < Y_SUELO && !saltando) {
                saltando = true;
            }
        }
    }

}
