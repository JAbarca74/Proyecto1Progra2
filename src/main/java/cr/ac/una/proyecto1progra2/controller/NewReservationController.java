package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.DTO.UsuariosDto;
import cr.ac.una.proyecto1progra2.model.CoworkingSpaces;
import cr.ac.una.proyecto1progra2.service.ReservationsService;
import cr.ac.una.proyecto1progra2.service.SpacesService;
import cr.ac.una.proyecto1progra2.util.FlowController;
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
import javafx.scene.Node;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.util.Arrays;

public class NewReservationController extends Controller implements Initializable {
    @FXML private DatePicker DatePickerDIasDIasReservaciones;
    @FXML private ComboBox<LocalTime> ComboBoxHoraIncio1;
    @FXML private ComboBox<LocalTime> ComboBoxHoraFin1;
    @FXML private ComboBox<String> ComboBoxPiso;
    @FXML private GridPane gridMatrix;
    @FXML private Label LabelNombreUsuario;
    @FXML private Label LabelPiso;
@FXML private Label LabelCanEscritorios;
@FXML private Label LabelCanSalasComunes;
@FXML private Label LabelCantAreasComunes;
@FXML private Label LabelCantEspaciosLibres;
    
    
public void forzarRecargaEspacios() {
    buscarEspacios();
}
    private ReservationsService reservationsService = new ReservationsService();
    private SpacesService spacesService = new SpacesService();
    private int pisoActual = 0;

    @Override
public void initialize(URL url, ResourceBundle resourceBundle) {
    DatePickerDIasDIasReservaciones.setValue(LocalDate.now());

    LocalTime t = LocalTime.of(7, 0);
    while (!t.isAfter(LocalTime.of(21, 0))) {
        ComboBoxHoraIncio1.getItems().add(t);
        ComboBoxHoraFin1.getItems().add(t);
        t = t.plusMinutes(30);
    }

    ComboBoxHoraIncio1.setValue(LocalTime.of(8, 0));
    int idx = ComboBoxHoraIncio1.getItems().indexOf(ComboBoxHoraIncio1.getValue());
    if (idx >= 0 && idx < ComboBoxHoraFin1.getItems().size() - 1) {
        ComboBoxHoraFin1.setValue(ComboBoxHoraFin1.getItems().get(idx + 1));
    }

configurarColoresCalendario();
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

    ComboBoxPiso.getItems().setAll("Piso 0", "Piso 1", "Piso 2", "Piso 3");
    ComboBoxPiso.getSelectionModel().selectFirst();
    ComboBoxPiso.valueProperty().addListener((obs, o, n) -> buscarEspacios());

    buscarEspacios();
    
}


private void reproducirSonido(String nombreArchivo) {
    try {
        AudioClip sonido = new AudioClip(getClass().getResource("/cr/ac/una/proyecto1progra2/resources/" + nombreArchivo).toExternalForm());
        sonido.play();
    } catch (Exception e) {
        System.out.println("Error al reproducir sonido: " + e.getMessage());
    }
}
private void cargarContadoresPorPiso(int piso) {
    List<SpaceVisual> espacios = spacesService.obtenerEspaciosConPosicion()
        .stream()
        .filter(e -> e.getSpace().getNombre() != null && e.getSpace().getNombre().contains("P" + piso))
        .collect(Collectors.toList());

    int escritorios = 0, salas = 0, areas = 0, libres = 0;

    for (SpaceVisual espacio : espacios) {
        String nombre = espacio.getSpace().getNombre().toLowerCase();
        if (nombre.startsWith("e -")) escritorios++;
        else if (nombre.startsWith("s -")) salas++;
        else if (nombre.startsWith("a -")) areas++;
        else if (nombre.startsWith("l -")) libres++;
    }

    LabelPiso.setText("Piso " + piso);
    LabelCanEscritorios.setText(String.valueOf(escritorios));
    LabelCanSalasComunes.setText(String.valueOf(salas));
    LabelCantAreasComunes.setText(String.valueOf(areas));
    LabelCantEspaciosLibres.setText(String.valueOf(libres));
}

 private void buscarEspacios() {
    LocalDate fecha = DatePickerDIasDIasReservaciones.getValue();
    LocalTime horaInicio = ComboBoxHoraIncio1.getValue();
    LocalTime horaFin = ComboBoxHoraFin1.getValue();
    pisoActual = obtenerPisoSeleccionado(); 

    if (fecha == null || horaInicio == null || horaFin == null || !horaInicio.isBefore(horaFin)) {
        Utilities.showAlert(Alert.AlertType.WARNING, "Datos inválidos", "Ingrese fecha y horas válidas.");
        return;
    }

    cargarContadoresPorPiso(pisoActual);
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

    if (coworkingSpaces.size() != espaciosEnPiso.size()) {
        List<Long> idsCoworking = coworkingSpaces.stream()
                .map(cs -> cs.getSpaceId().getId())
                .toList();
        List<Long> idsFaltantes = espaciosEnPiso.stream()
                .filter(id -> !idsCoworking.contains(id))
                .toList();

        System.out.println("Espacios sin CoworkingSpace:");
        idsFaltantes.forEach(id -> System.out.println(" - Space ID sin asociar: " + id));

        Utilities.showAlert(Alert.AlertType.ERROR,
                "Error en espacios",
                "Uno o más espacios del piso no tienen un CoworkingSpace asociado. No se puede completar la reserva.");
        return;
    }

    for (CoworkingSpaces cs : coworkingSpaces) {
        reservationsService.guardarReserva(userId, cs.getId(), fecha, horaInicio, horaFin);
    }

    reproducirSonido("intro-sound-bell-269297-_1_.wav");
    mostrarFactura(); // NUEVO
    Utilities.showAlert(Alert.AlertType.INFORMATION, "Reserva completada", "¡Reserva registrada para el piso completo!");
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

   
    private int obtenerPisoSeleccionado() {
        return ComboBoxPiso.getSelectionModel().getSelectedIndex();
    }
    
    

private void configurarColoresCalendario() {
    DatePickerDIasDIasReservaciones.setDayCellFactory(picker -> new DateCell() {
        @Override
        public void updateItem(LocalDate date, boolean empty) {
            super.updateItem(date, empty);

            if (empty || date == null) {
                setStyle("");
                return;
            }

            double ocupacion = calcularPorcentajeOcupacion(date);

            if (ocupacion >= 0.8) {
                setStyle("-fx-background-color: #ffcccc;"); 
            } else if (ocupacion >= 0.4) {
                setStyle("-fx-background-color: #fff0b3;"); 
            } else {
                setStyle("-fx-background-color: #ccffcc;"); 
            }
        }
    });
}

private double calcularPorcentajeOcupacion(LocalDate fecha) {
    int totalEspacios = (int) spacesService.obtenerEspaciosConPosicion().stream()
            .filter(e -> e.getSpace().getNombre().contains("P" + pisoActual))
            .count();

    List<Long> ocupados = reservationsService.obtenerEspaciosOcupados(fecha,
            LocalTime.of(7, 0), LocalTime.of(21, 0));

    long ocupadosEnPiso = ocupados.stream()
            .filter(id -> spacesService.obtenerEspaciosConPosicion().stream()
                    .anyMatch(e -> e.getSpace().getId().equals(id) &&
                            e.getSpace().getNombre().contains("P" + pisoActual)))
            .count();

    if (totalEspacios == 0) return 0.0;
    return (double) ocupadosEnPiso / totalEspacios;
}

  private StackPane crearCeldaEspacio(SpaceVisual espacio, boolean estaOcupado) {
    StackPane stack = new StackPane();
    stack.getStyleClass().add("stack-celda");

    Rectangle rect = new Rectangle(100 * espacio.getColSpan(), 70 * espacio.getRowSpan());
    rect.setArcWidth(40);
    rect.setArcHeight(40);
    rect.setStroke(Color.TRANSPARENT);
    rect.getStyleClass().add("celda-rect");

    if (estaOcupado) {
        rect.setFill(Color.GRAY);
    } else {
        String nombre = espacio.getSpace().getNombre().toLowerCase();
        if (nombre.contains("sala")) rect.setFill(Color.web("#2196F3"));
        else if (nombre.contains("área")) rect.setFill(Color.web("#9C27B0"));
        else if (nombre.contains("libre")) rect.setFill(Color.web("#4CAF50"));
        else if (nombre.contains("e")) rect.setFill(Color.web("#FF5722"));
        else rect.setFill(Color.LIGHTGRAY);
    }

    Text text = new Text(espacio.getSpace().getNombre());
    text.setFill(Color.WHITE);
    text.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
    stack.getChildren().addAll(rect, text);

    stack.setOnMouseClicked(event -> {
        if (estaOcupado) {
            reproducirSonido("correct-cbt.wav");
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

        CoworkingSpaces coworking = spacesService.obtenerCoworkingSpacePorSpaceId(espacio.getSpace().getId());

        if (coworking == null) {
            reproducirSonido("correct-cbt.wav");
            Utilities.showAlert(Alert.AlertType.ERROR, "Error", "Este espacio no tiene CoworkingSpace asociado.");
            return;
        }

        boolean traslape = !reservationsService
                .buscarReservasTraslapadas(coworking.getId(), fecha, horaInicio, horaFin)
                .isEmpty();

        if (traslape) {
            reproducirSonido("correct-cbt.wav");
            Utilities.showAlert(Alert.AlertType.WARNING, "Horario en conflicto", "Ya existe una reserva para este espacio en ese horario.");
            return;
        }

        boolean guardado = reservationsService.guardarReserva(userId, coworking.getId(), fecha, horaInicio, horaFin);
if (guardado) {
    reproducirSonido("intro-sound-bell-269297-_1_.wav");
    List<CoworkingSpaces> espacioIndividual = Arrays.asList(coworking); // NUEVO
    mostrarFactura(); // NUEVO
   
    cargarMatrizDeEspaciosDisponibles(fecha, horaInicio, horaFin);
}
        else {
            reproducirSonido("correct-cbt.wav");
            Utilities.showAlert(Alert.AlertType.ERROR, "Error inesperado", "No se pudo registrar la reserva. Intente nuevamente.");
        }
    });

    return stack;
}
private void mostrarFactura() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/cr/ac/una/proyecto1progra2/view/Invoice.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    } catch (Exception e) {
        e.printStackTrace();
         Utilities.showAlert(Alert.AlertType.WARNING, "conflicto", "no se puedo abrir la factura.");
    }
}
@Override
public Stage getStage() {
    try {
        return (Stage) gridMatrix.getScene().getWindow();
    } catch (Exception e) {
        return null;
    }
}
    @Override
    public void initialize() {
    }
}
