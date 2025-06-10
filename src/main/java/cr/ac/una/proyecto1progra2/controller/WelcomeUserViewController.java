package cr.ac.una.proyecto1progra2.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class WelcomeUserViewController extends Controller implements Initializable {

    @FXML private ImageView              imgAvatar;
    @FXML private Label                  lblTip;
    @FXML private Label                  lblEvento;
    @FXML private Label                  lblBeneficio;
    @FXML private ListView<String>       lstAvisos;
    @FXML private Label                  lblLastUpdate;

    private Timeline ticker;   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            imgAvatar.setImage(new Image(
                getClass().getResource(
                    "/cr/ac/una/proyecto1progra2/resources/user_avatar.png")
                .toExternalForm()));
        } catch (Exception ignore) { }

        lstAvisos.getItems().setAll(
            "Recuerda mantener tu estación limpia ✨",
            "Configura el modo silencio en llamadas 📵",
            "Prueba los Focus Pods para concentrarte 🎧",
            "Café de especialidad gratis los viernes ☕",
            "Networking brunch cada jueves a las 10 a.m. 🥐",
            "Lockers inteligentes disponibles en recepción 🔐",
            "¿Necesitas apoyo? Pregunta a nuestro staff 😊",
            "FlexSpace es 100 % eco-friendly ♻️",
            "Descarga la app móvil para reservar al instante 📱",
            "Se inaugura la terraza verde este mes 🌱"
        );

        ticker = new Timeline(new KeyFrame(Duration.seconds(4), e -> {
            if (!lstAvisos.getItems().isEmpty()) {
                String first = lstAvisos.getItems().remove(0);
                lstAvisos.getItems().add(first);
            }
        }));
        ticker.setCycleCount(Timeline.INDEFINITE);
        ticker.play();

        lblLastUpdate.setText(
            LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("d 'de' MMMM yyyy, HH:mm:ss"))
            + "  |  Última actualización");
    }

    @Override
    public void initialize() {
        
    }
}
