package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.service.ReservationsService;
import cr.ac.una.proyecto1progra2.service.UsuariosService;
import cr.ac.una.proyecto1progra2.service.SpacesService;
import cr.ac.una.proyecto1progra2.util.Respuesta;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class WelcomeViewController extends Controller implements Initializable {

    @FXML private ImageView imgAvatar;
    @FXML private Label     lblReservasHoy;
    @FXML private Label     lblEspacios;
    @FXML private Label     lblUsuarios;
    @FXML private Label     lblOcupacion;
    @FXML private Label     lblLastUpdate;

    private final ReservationsService reservationsService = new ReservationsService();
    private final SpacesService       spacesService       = new SpacesService();
    private final UsuariosService     usuariosService     = new UsuariosService();

    private Timeline refresher;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Avatar opcional
        try {
            imgAvatar.setImage(new Image(
                getClass().getResource("/cr/ac/una/proyecto1progra2/resources/admin_avatar.png")
                         .toExternalForm()
            ));
        } catch (Exception ignore) {}

        // Timeline para refresco cada 30 segundos
        refresher = new Timeline(
            new KeyFrame(Duration.ZERO, e -> actualizarMetricas()),
            new KeyFrame(Duration.seconds(30))
        );
        refresher.setCycleCount(Timeline.INDEFINITE);
        refresher.play();

        // Primera carga
        actualizarMetricas();
    }

    private void actualizarMetricas() {
        // 1) Reservas de hoy
        long hoyCount = reservationsService.listarTodasView()
                              .stream()
                              .filter(r -> r.getReservationDate().isEqual(LocalDate.now()))
                              .count();

        // 2) Espacios totales
        Respuesta respSpaces = spacesService.listarSpaces(); // devuelve Respuesta con List<SpacesDto> en "Spaces"
        int totalEspacios = 0;
        if (respSpaces.isSuccess()) {
            List<?> list = (List<?>) respSpaces.getResultado("Spaces");
            totalEspacios = list != null ? list.size() : 0;
        }

        // 3) Usuarios totales
        Respuesta respUsers = usuariosService.listarUsuarios(); // devuelve Respuesta con List<UsuariosDto> en "Usuarios"
        int totalUsuarios = 0;
        if (respUsers.isSuccess()) {
            List<?> list = (List<?>) respUsers.getResultado("Usuarios");
            totalUsuarios = list != null ? list.size() : 0;
        }

        // 4) Ocupación = reservasHoy / espaciosTotales
        String ocupTxt = totalEspacios == 0
            ? "0 %"
            : String.format("%.0f %%", (hoyCount * 100.0) / totalEspacios);

        // Pintar en UI
        lblReservasHoy.setText(String.valueOf(hoyCount));
        lblEspacios   .setText(String.valueOf(totalEspacios));
        lblUsuarios   .setText(String.valueOf(totalUsuarios));
        lblOcupacion  .setText(ocupTxt);

        // Fecha de última actualización
        lblLastUpdate.setText(
            LocalDateTime.now()
                         .format(DateTimeFormatter.ofPattern("d 'de' MMMM yyyy, HH:mm:ss"))
            + "  |  Última actualización"
        );
    }

    @Override
    public void initialize() { /* no usado */ }
}
