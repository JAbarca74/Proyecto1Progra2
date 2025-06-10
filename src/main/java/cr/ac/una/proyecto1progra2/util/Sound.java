package cr.ac.una.proyecto1progra2.util;

import java.net.URL;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sound {
    private static MediaPlayer player;

    
    public static void playEatPoint() {
        playOnce("punto.mp3");
    }

    
    public static void playLose() {
        playOnce("pacman-dies.mp3");
    }

    
    private static void playOnce(String fileName) {
        stop();  
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

   
    private static void stop() {
        if (player != null) {
            player.stop();
            player = null;
        }
    }
}
