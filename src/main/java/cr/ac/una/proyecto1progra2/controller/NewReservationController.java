package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.DTO.UsuariosDto;
import cr.ac.una.proyecto1progra2.model.CoworkingSpaces;
import cr.ac.una.proyecto1progra2.service.ReservationsService;
import cr.ac.una.proyecto1progra2.service.SpacesService;
import cr.ac.una.proyecto1progra2.util.SpaceVisual;
import cr.ac.una.proyecto1progra2.util.UserManager;
import cr.ac.una.proyecto1progra2.util.Utilities;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import javax.persistence.EntityManager;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class NewReservationController extends Controller implements Initializable {
    @FXML private DatePicker DatePickerDIasDIasReservaciones;
    @FXML private ComboBox<LocalTime> ComboBoxHoraIncio1;
    @FXML private ComboBox<LocalTime> ComboBoxHoraFin1;
    @FXML private ComboBox<String> ComboBoxPiso;
    @FXML private GridPane gridMatrix;
    @FXML private Label LabelNombreUsuario;

    private ReservationsService reservationsService = new ReservationsService();
    private SpacesService spacesService = new SpacesService();
    private int pisoActual = 0;

    @Override
public void initialize(URL url, ResourceBundle resourceBundle) {
    // 1) Fecha por defecto: hoy
    DatePickerDIasDIasReservaciones.setValue(LocalDate.now());

    // 2) Horarios cada 30 minutos desde 07:00 hasta 21:00
    LocalTime t = LocalTime.of(7, 0);
    while (!t.isAfter(LocalTime.of(21, 0))) {
        ComboBoxHoraIncio1.getItems().add(t);
        ComboBoxHoraFin1.getItems().add(t);
        t = t.plusMinutes(30);
    }

    // 3) Valores iniciales: 08:00 → 08:00, fin al siguiente slot → 08:30
    ComboBoxHoraIncio1.setValue(LocalTime.of(8, 0));
    int idx = ComboBoxHoraIncio1.getItems().indexOf(ComboBoxHoraIncio1.getValue());
    if (idx >= 0 && idx < ComboBoxHoraFin1.getItems().size() - 1) {
        ComboBoxHoraFin1.setValue(ComboBoxHoraFin1.getItems().get(idx + 1));
    }

    // 4) Listeners para recargar y ajustar fin automáticamente
    DatePickerDIasDIasReservaciones.valueProperty().addListener((obs, o, n) -> buscarEspacios());
    ComboBoxHoraFin1.valueProperty().addListener((obs, o, n) -> buscarEspacios());

    ComboBoxHoraIncio1.valueProperty().addListener((obs, oldVal, newVal) -> {
        if (newVal != null) {
            int i = ComboBoxHoraIncio1.getItems().indexOf(newVal);
            if (i >= 0 && i < ComboBoxHoraFin1.getItems().size() - 1) {
                ComboBoxHoraFin1.setValue(ComboBoxHoraFin1.getItems().get(i + 1));
            }
        }
        buscarEspacios();
    });

    // 5) Piso y su listener
    ComboBoxPiso.getItems().setAll("Piso 0", "Piso 1", "Piso 2", "Piso 3");
    ComboBoxPiso.getSelectionModel().selectFirst();
    ComboBoxPiso.valueProperty().addListener((obs, o, n) -> buscarEspacios());

    // 6) Carga inicial
    buscarEspacios();
}

    private void buscarEspacios() {
        LocalDate fecha = DatePickerDIasDIasReservaciones.getValue();
        LocalTime horaInicio = ComboBoxHoraIncio1.getValue();
        LocalTime horaFin = ComboBoxHoraFin1.getValue();

        if (fecha == null || horaInicio == null || horaFin == null || !horaInicio.isBefore(horaFin)) {
            Utilities.showAlert(Alert.AlertType.WARNING, "Datos inv\u00e1lidos", "Ingrese fecha y horas v\u00e1lidas.");
            return;
        }
        cargarMatrizDeEspaciosDisponibles(fecha, horaInicio, horaFin);
    }

    @FXML
private void guardarReserva() {
    LocalDate fecha = DatePickerDIasDIasReservaciones.getValue();
    LocalTime horaInicio = ComboBoxHoraIncio1.getValue();
    LocalTime horaFin = ComboBoxHoraFin1.getValue();

    if (fecha == null || horaInicio == null || horaFin == null) {
        Utilities.showAlert(Alert.AlertType.WARNING, "Datos incompletos", "Complete todos los campos para reservar.");
        return;
    }

    Long userId = UserManager.getCurrentUser().getId();

    List<Long> espaciosEnPiso = spacesService.obtenerEspaciosConPosicion().stream()
            .filter(e -> e.getSpace().getNombre().contains("P" + pisoActual))
            .map(e -> e.getSpace().getId())
            .toList();

    boolean ocupado = espaciosEnPiso.stream().anyMatch(id ->
            !reservationsService.buscarReservasTraslapadas(id, fecha, horaInicio, horaFin).isEmpty());

    if (ocupado) {
        Utilities.showAlert(Alert.AlertType.WARNING, "Piso ocupado", "Ya existe una reserva en este piso y horario.");
        return;
    }

    List<CoworkingSpaces> coworkingSpaces = spacesService.obtenerCoworkingSpacesPorSpaceIds(espaciosEnPiso);

    // 🛑 Validación extra: ¿hay espacios sin coworking asociado?
    if (coworkingSpaces.size() != espaciosEnPiso.size()) {
    List<Long> idsCoworking = coworkingSpaces.stream()
            .map(cs -> cs.getSpaceId().getId())
            .toList();
    List<Long> idsFaltantes = espaciosEnPiso.stream()
            .filter(id -> !idsCoworking.contains(id))
            .toList();

    System.out.println("❌ Espacios sin CoworkingSpace:");
    idsFaltantes.forEach(id -> System.out.println(" - Space ID sin asociar: " + id));

    Utilities.showAlert(Alert.AlertType.ERROR,
        "Error en espacios",
        "Uno o más espacios del piso no tienen un CoworkingSpace asociado. No se puede completar la reserva.");
    return;
}

    for (CoworkingSpaces cs : coworkingSpaces) {
        reservationsService.guardarReserva(userId, cs.getId(), fecha, horaInicio, horaFin);
    }

    Utilities.showAlert(Alert.AlertType.INFORMATION, "Reserva completada", "\u00a1Reserva registrada para el piso completo!");
    cargarMatrizDeEspaciosDisponibles(fecha, horaInicio, horaFin);
}
    private void cargarMatrizDeEspaciosDisponibles() {
        LocalDate fecha = DatePickerDIasDIasReservaciones.getValue();
        if (fecha == null) fecha = LocalDate.now();
        LocalTime horaInicio = ComboBoxHoraIncio1.getValue();
        if (horaInicio == null) horaInicio = LocalTime.of(8, 0);
        LocalTime horaFin = ComboBoxHoraFin1.getValue();
        if (horaFin == null) horaFin = LocalTime.of(9, 0);
        cargarMatrizDeEspaciosDisponibles(fecha, horaInicio, horaFin);
    }

    private void cargarMatrizDeEspaciosDisponibles(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        gridMatrix.getChildren().clear();
        int piso = obtenerPisoSeleccionado();

        List<Long> ocupados = reservationsService.obtenerEspaciosOcupados(fecha, horaInicio, horaFin);
        List<SpaceVisual> espacios = spacesService.obtenerEspaciosConPosicion()
                .stream()
                .filter(e -> {
                    String nombre = e.getSpace().getNombre();
                    return nombre != null && nombre.toLowerCase().contains("p" + piso);
                })
                .toList();

        for (SpaceVisual espacio : espacios) {
            boolean estaOcupado = ocupados.contains(espacio.getSpace().getId());
            StackPane celda = crearCeldaEspacio(espacio, estaOcupado);
            gridMatrix.add(celda,
                    espacio.getColumn(),
                    espacio.getRow(),
                    espacio.getColSpan(),
                    espacio.getRowSpan());
        }
    }

    private StackPane crearCeldaEspacio(SpaceVisual espacio, boolean estaOcupado) {
    StackPane stack = new StackPane();
    Rectangle rect = new Rectangle(100 * espacio.getColSpan(), 60 * espacio.getRowSpan());
    rect.setArcWidth(0);
    rect.setArcHeight(10);

    if (estaOcupado) {
        rect.setFill(Color.GRAY);
    } else {
        String nombre = espacio.getSpace().getNombre().toLowerCase();
        if (nombre.contains("sala")) rect.setFill(Color.CRIMSON);
        else if (nombre.contains("área")) rect.setFill(Color.DARKGREEN);
        else if (nombre.contains("libre")) rect.setFill(Color.GOLD);
        else if (nombre.contains("e")) rect.setFill(Color.DODGERBLUE);
        else rect.setFill(Color.LIGHTGRAY);
    }

    rect.setStroke(Color.BLACK);
    Text text = new Text(espacio.getSpace().getNombre());
    text.setFill(Color.WHITE);
    stack.getChildren().addAll(rect, text);

    // ✅ Clic para reservar este espacio individualmente
    stack.setOnMouseClicked(event -> {
        if (estaOcupado) {
            Utilities.showAlert(Alert.AlertType.WARNING, "Espacio ocupado", "Este espacio ya está reservado.");
            return;
        }

        LocalDate fecha = DatePickerDIasDIasReservaciones.getValue();
        LocalTime horaInicio = ComboBoxHoraIncio1.getValue();
        LocalTime horaFin = ComboBoxHoraFin1.getValue();
        Long userId = UserManager.getCurrentUser().getId();

        if (fecha == null || horaInicio == null || horaFin == null) {
            Utilities.showAlert(Alert.AlertType.WARNING, "Datos incompletos", "Complete todos los campos para reservar.");
            return;
        }

        CoworkingSpaces coworking = espacio.getSpace().getCoworkingSpace();
        if (coworking == null) {
            Utilities.showAlert(Alert.AlertType.ERROR, "Error", "Este espacio no tiene CoworkingSpace asociado.");
            return;
        }

        boolean guardado = reservationsService.guardarReserva(userId, coworking.getId(), fecha, horaInicio, horaFin);
        if (guardado) {
            Utilities.showAlert(Alert.AlertType.INFORMATION, "Reserva exitosa", "Reserva registrada correctamente.");
            cargarMatrizDeEspaciosDisponibles(fecha, horaInicio, horaFin);
        } else {
            Utilities.showAlert(Alert.AlertType.ERROR, "Error", "No se pudo registrar la reserva.");
        }
    });

    return stack;
}
    private int obtenerPisoSeleccionado() {
        return ComboBoxPiso.getSelectionModel().getSelectedIndex();
    }

    @Override
    public void initialize() {
    }
}
