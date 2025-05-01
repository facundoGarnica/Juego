package carne.Clases;

import javafx.scene.layout.AnchorPane;

public class Camara {

    private Personaje personaje1;
    private Personaje personaje2;
    private AnchorPane root;

    private double desplazamientoActual = 0;
    private final double suavizadoBase = 0.1; // Valor base de suavizado
    private final long INTERVALO_CAMBIO_FRAME = 100_000_000; // en nanosegundos

    private final double UMEBRAL_BORDE = 100; // Umbral de distancia desde el borde para mover la cámara
    private final double ANCHO_PANTALLA = 800; // Ancho de la pantalla (puedes ajustarlo)

    public Camara(AnchorPane root, Personaje p1, Personaje p2) {
        this.root = root;
        this.personaje1 = p1;
        this.personaje2 = p2;
    }

    public void actualizar() {
        double centro1 = personaje1.getContenedor().getLayoutX() + personaje1.getContenedor().getWidth() / 2;
        double centro2 = personaje2.getContenedor().getLayoutX() + personaje2.getContenedor().getWidth() / 2;

        double centroPromedio = (centro1 + centro2) / 2;

        // Queremos que el centro de la pantalla siga a los personajes
        double objetivo = -(centroPromedio - ANCHO_PANTALLA / 2);

        // Convertir el intervalo de tiempo a segundos
        double tiempoEnSegundos = INTERVALO_CAMBIO_FRAME / 1_000_000_000.0;

        // Ajustar suavizado según el intervalo de fotogramas
        double suavizado = suavizadoBase * tiempoEnSegundos;

        // Obtener las posiciones de los personajes respecto a los bordes de la pantalla
        double bordeIzq1 = personaje1.getContenedor().getLayoutX();
        double bordeDer1 = bordeIzq1 + personaje1.getContenedor().getWidth();
        double bordeIzq2 = personaje2.getContenedor().getLayoutX();
        double bordeDer2 = bordeIzq2 + personaje2.getContenedor().getWidth();

        // Verificar si ambos personajes están cerca del borde izquierdo o derecho
        boolean ambosCercaIzq = bordeIzq1 <= UMEBRAL_BORDE && bordeIzq2 <= UMEBRAL_BORDE;
        boolean ambosCercaDer = bordeDer1 >= ANCHO_PANTALLA - UMEBRAL_BORDE && bordeDer2 >= ANCHO_PANTALLA - UMEBRAL_BORDE;

        // La cámara solo se mueve si ambos personajes están cerca de los bordes
        if (ambosCercaIzq || ambosCercaDer) {
            // Interpolación suave
            desplazamientoActual += (objetivo - desplazamientoActual) * suavizado;
        }

        // Establecer la nueva posición de la cámara
        root.setTranslateX(desplazamientoActual);
    }
}
