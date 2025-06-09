package cr.ac.una.proyecto1progra2.util;

import java.net.URL;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicManager {

    private static MediaPlayer backgroundPlayer;
    private static MediaPlayer effectPlayer;

    /** Llama esto al arrancar la app para música de fondo. */
    public static void playBackground(String fileName) {
        stopBackground();
        URL res = MusicManager.class.getResource(
            "/cr/ac/una/proyecto1progra2/mp3/" + fileName
        );
        if (res == null) {
            System.err.println("No encontré: " + fileName);
            return;
        }
        Media m = new Media(res.toExternalForm());
        backgroundPlayer = new MediaPlayer(m);
        backgroundPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        backgroundPlayer.play();
    }

    public static void pauseBackgroundMusic() {
        if (backgroundPlayer != null) backgroundPlayer.pause();
    }

    public static void resumeBackgroundMusic() {
        if (backgroundPlayer != null) backgroundPlayer.play();
    }

    public static void stopBackground() {
        if (backgroundPlayer != null) {
            backgroundPlayer.stop();
            backgroundPlayer = null;
        }
    }

    /** Efecto puntual (come, muerte, etc). */
    public static void playEffect(String fileName, boolean loop) {
        stopEffect();
        URL res = MusicManager.class.getResource(
            "/cr/ac/una/proyecto1progra2/mp3/" + fileName
        );
        if (res == null) {
            System.err.println("No encontré efecto: " + fileName);
            return;
        }
        Media m = new Media(res.toExternalForm());
        effectPlayer = new MediaPlayer(m);
        effectPlayer.setCycleCount(loop ? MediaPlayer.INDEFINITE : 1);
        effectPlayer.play();
    }

    public static void stopEffect() {
        if (effectPlayer != null) {
            effectPlayer.stop();
            effectPlayer = null;
        }
    }
}
