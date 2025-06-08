package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.DTO.ReservationViewDto;
import cr.ac.una.proyecto1progra2.DTO.UsuariosDto;
import cr.ac.una.proyecto1progra2.service.ReservationsService;
import cr.ac.una.proyecto1progra2.service.UsuariosService;
import cr.ac.una.proyecto1progra2.util.Respuesta;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ListCell;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class ReservationManagementController extends Controller implements Initializable {

    @FXML private DatePicker datePickerFiltro;
    @FXML private ComboBox<UsuariosDto> comboUsuarios;
    @FXML private ComboBox<String> comboPiso;

    @FXML private TableView<ReservationViewDto> tablaReservas;
    @FXML private TableColumn<ReservationViewDto, String> colUsuario;
    @FXML private TableColumn<ReservationViewDto, String> colEspacio;
    @FXML private TableColumn<ReservationViewDto, String> colPiso;
    @FXML private TableColumn<ReservationViewDto, LocalDate> colFecha;
    @FXML private TableColumn<ReservationViewDto, String> colInicio;
    @FXML private TableColumn<ReservationViewDto, String> colFin;
    @FXML private TableColumn<ReservationViewDto, String> colEstado;
    @FXML private TableColumn<ReservationViewDto, Void> colAcciones;

    private final ReservationsService reservationsService = new ReservationsService();
    private final UsuariosService    usuariosService    = new UsuariosService();

    private final ObservableList<ReservationViewDto> reservas = FXCollections.observableArrayList();
    private final ObservableList<UsuariosDto>         usuarios = FXCollections.observableArrayList();
    private final ObservableList<String>              pisos    = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 1) ComboUsuarios → mostrar sólo username
        comboUsuarios.setConverter(new StringConverter<UsuariosDto>() {
            @Override public String toString(UsuariosDto u) { return u != null ? u.getUsername() : ""; }
            @Override public UsuariosDto fromString(String s) { return null; }
        });
        comboUsuarios.setCellFactory(lv -> new ListCell<UsuariosDto>() {
            @Override protected void updateItem(UsuariosDto u, boolean empty) {
                super.updateItem(u, empty);
                setText(empty || u == null ? "" : u.getUsername());
            }
        });

        // 2) Pisos fijos P0–P3
        pisos.setAll("P0","P1","P2","P3");
        comboPiso.setItems(pisos);

        // 3) Columnas de la tabla
        colUsuario.setCellValueFactory(c ->
            new ReadOnlyStringWrapper(c.getValue().getUserName()));
        colEspacio.setCellValueFactory(c ->
            new ReadOnlyStringWrapper(c.getValue().getSpaceName()));
        colPiso.setCellValueFactory(c ->
            new ReadOnlyStringWrapper(c.getValue().getPiso()));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
        colInicio.setCellValueFactory(c ->
            new ReadOnlyStringWrapper(c.getValue().getStartTime().toString()));
        colFin.setCellValueFactory(c ->
            new ReadOnlyStringWrapper(c.getValue().getEndTime().toString()));
        colEstado.setCellValueFactory(c ->
            new ReadOnlyStringWrapper(c.getValue().getEstado()));

        // 4) Botón Eliminar en cada fila
        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button b = new Button("Eliminar");
            {
                b.setOnAction(evt -> {
                    ReservationViewDto dto = getTableView().getItems().get(getIndex());
                    if (reservationsService.eliminarReserva(dto.getId())) {
                        reservas.remove(dto);
                    } else {
                        new Alert(Alert.AlertType.ERROR, "No se pudo eliminar").showAndWait();
                    }
                });
            }
            @Override protected void updateItem(Void v, boolean emp) {
                super.updateItem(v, emp);
                setGraphic(emp ? null : b);
            }
        });

        tablaReservas.setItems(reservas);

        cargarUsuarios();
        cargarTodas();
    }

    private void cargarUsuarios() {
        Respuesta r = usuariosService.listarUsuarios();
        if (r.isSuccess()) {
            //noinspection unchecked
            usuarios.setAll((List<UsuariosDto>)r.getResultado("Usuarios"));
            comboUsuarios.setItems(usuarios);
        }
    }

    private void cargarTodas() {
        reservas.setAll(reservationsService.listarTodasView());
    }

    @FXML
    private void onBuscar(ActionEvent ev) {
        LocalDate f = datePickerFiltro.getValue();
        UsuariosDto u = comboUsuarios.getValue();
        String piso  = comboPiso.getValue();
        Long uid     = u != null ? u.getId() : null;
        reservas.setAll(reservationsService.listarPorFiltros(f, uid, piso));
    }

    @FXML
    private void onLimpiar(ActionEvent ev) {
        datePickerFiltro.setValue(null);
        comboUsuarios.getSelectionModel().clearSelection();
        comboPiso  .getSelectionModel().clearSelection();
        cargarTodas();
    }

    @Override
    public void initialize() {
        
    }
}
