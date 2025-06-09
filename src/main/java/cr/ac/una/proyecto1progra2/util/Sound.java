package cr.ac.una.proyecto1progra2.util;

import java.net.URL;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sound {
    private static MediaPlayer player;

    /** Reproduce un efecto corto: comer punto */
    public static void playEatPoint() {
        playOnce("punto.mp3");
    }

    /** Reproduce el sonido de muerte */
    public static void playLose() {
        playOnce("pacman-dies.mp3");
    }

    /** Lógica interna para reproducir un MP3 una sola vez */
    private static void playOnce(String fileName) {
        stop();  // detenemos cualquier reproducción previa
        URL resource = Sound.class.getResource(
            "/cr/ac/una/proyecto1progra2/mp3/" + fileName
        );
        if (resource == null) {
            System.err.println("No se encontró el archivo de sonido: " + fileName);
            return;
        }
        Media media = new Media(resource.toExternalForm());
        player = new MediaPlayer(media);
        player.play();
    }

    /** Detiene cualquier efecto que esté sonando */
    private static void stop() {
        if (player != null) {
            player.stop();
            player = null;
        }
    }
}
