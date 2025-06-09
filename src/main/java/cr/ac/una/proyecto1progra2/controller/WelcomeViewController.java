package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.service.ReservationsService;
import cr.ac.una.proyecto1progra2.service.SpacesService;
import cr.ac.una.proyecto1progra2.service.UsuariosService;
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

    /* ──────────── FXML ──────────── */
    @FXML private ImageView imgAvatar;
    @FXML private Label     lblReservasHoy;
    @FXML private Label     lblEspacios;
    @FXML private Label     lblUsuarios;
    @FXML private Label     lblOcupacion;
    @FXML private Label     lblLastUpdate;
    @FXML private ImageView imgWeatherIcon;
    @FXML private Label     lblWeather;
    @FXML private javafx.scene.control.ListView<String> lstNews;

    /* ──────────── Servicios ──────────── */
    private final ReservationsService reservationsService = new ReservationsService();
    private final SpacesService       spacesService       = new SpacesService();
    private final UsuariosService     usuariosService     = new UsuariosService();

    private Timeline refresher;   // refresca métricas
    private Timeline ticker;      // rota titulares

    /* ──────────── init ──────────── */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        /* Avatar opcional */
        try {
            imgAvatar.setImage(new Image(
                getClass()
                    .getResource("/cr/ac/una/proyecto1progra2/resources/admin_avatar.png")
                    .toExternalForm()));
        } catch (Exception ignore) {}

        /* Refresco de métricas cada 30 s */
        refresher = new Timeline(
            new KeyFrame(Duration.ZERO, e -> actualizarMetricas()),
            new KeyFrame(Duration.seconds(30)));
        refresher.setCycleCount(Timeline.INDEFINITE);
        refresher.play();

        /* Primera carga + panel de clima/noticias */
        actualizarMetricas();
        initFakeNewsAndWeather();
    }

    /* ──────────── Métricas dashboard ──────────── */
    private void actualizarMetricas() {

        /* 1) Reservas de hoy */
        long hoyCount = reservationsService.listarTodasView().stream()
                           .filter(r -> r.getReservationDate()!=null
                                    && r.getReservationDate().isEqual(LocalDate.now()))
                           .count();

        /* 2) Espacios totales */
        Respuesta respSpaces = spacesService.listarSpaces();
        int totalEspacios = respSpaces.isSuccess()
                ? ((List<?>) respSpaces.getResultado("Spaces")).size() : 0;

        /* 3) Usuarios totales */
        Respuesta respUsers = usuariosService.listarUsuarios();
        int totalUsuarios = respUsers.isSuccess()
                ? ((List<?>) respUsers.getResultado("Usuarios")).size() : 0;

        /* 4) Ocupación % */
        String ocupTxt = totalEspacios == 0
                ? "0 %"
                : String.format("%.0f %%", (hoyCount * 100.0) / totalEspacios);

        /* Pintar en UI */
        lblReservasHoy.setText(String.valueOf(hoyCount));
        lblEspacios.setText(String.valueOf(totalEspacios));
        lblUsuarios.setText(String.valueOf(totalUsuarios));
        lblOcupacion.setText(ocupTxt);

        /* Tiempo de última actualización */
        lblLastUpdate.setText(
            LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("d 'de' MMMM yyyy, HH:mm:ss"))
            + "  |  Última actualización");
    }

    /* ──────────── Noticias + Clima ficticios ──────────── */
    private void initFakeNewsAndWeather() {

        /* Clima simulado */
        lblWeather.setText("Pérez Zeledón · 23 °C · Parcial nublado");
        try {
            imgWeatherIcon.setImage(new Image(
               getClass().getResource(
                 "/cr/ac/una/proyecto1progra2/resources/weather_partly.png").toExternalForm()));
        } catch (Exception ignore) {}

        /* Lista de 20 noticias/intereses */
        lstNews.getItems().setAll(
            "FlexSpace P.Z. inaugura terraza verde ☘️",
            "Nueva zona 'Focus Pods' para llamadas silenciosas 📞",
            "FlexSpace en el top-5 coworkings de Costa Rica 🏆",
            "Café de especialidad gratis los viernes ☕",
            "Taller de productividad: GTD para freelancers ✍️",
            "Mural colaborativo: pinta tu idea 💡",
            "Ranking: Espacio 12 el más reservado en julio",
            "Nuevas membresías nocturnas para programadores 🌙",
            "Tips: 3 ejercicios de estiramiento en tu escritorio 🧘",
            "Networking brunch este jueves a las 10 a.m. 🥐",
            "FlexSpace recibe certificación eco-friendly ♻️",
            "Se abre sala de realidad virtual para demos VR 🕶️",
            "Descuento del 20 % para estudiantes universitarios 🎓",
            "Hackathon local: inscríbete antes del 30 julio 👩‍💻",
            "Nuevo servicio de lockers inteligentes 🔐",
            "FlexSpace apoya feria agrícola de Pérez Zeledón 🍍",
            "Evento: FinTech meetup el 18 agosto 💳",
            "FlexSpace implementa paneles solares en el techo ☀️",
            "Clases de yoga al atardecer los miércoles 🧘‍♂️",
            "Se habilita app móvil para reservas instantáneas 📱"
        );

        /* Animación tipo ticker: rota cada 4 s */
        ticker = new Timeline(new KeyFrame(Duration.seconds(4), e -> {
            if (!lstNews.getItems().isEmpty()) {
                String first = lstNews.getItems().remove(0);
                lstNews.getItems().add(first);
            }
        }));
        ticker.setCycleCount(Timeline.INDEFINITE);
        ticker.play();

        /* Celda personalizada para estilo “tarjeta mini” */
        lstNews.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                }
            }
        });
    }

    /* (no usado, pero requerido por abstract Controller) */
    @Override public void initialize() {}
}
