package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.DTO.ReservationsDto;
import cr.ac.una.proyecto1progra2.DTO.UsuariosDto;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class ReservationManagementController extends Controller implements Initializable {

    @FXML private DatePicker datePickerFiltro;
    @FXML private ComboBox<UsuariosDto> comboUsuarios;
    @FXML private ComboBox<String> comboPiso;

    @FXML private TableView<ReservationsDto> tablaReservas;
    @FXML private TableColumn<ReservationsDto, Long> colUsuario;
    @FXML private TableColumn<ReservationsDto, Long> colEspacio;
    @FXML private TableColumn<ReservationsDto, String> colPiso;
    @FXML private TableColumn<ReservationsDto, LocalDate> colFecha;
    @FXML private TableColumn<ReservationsDto, LocalTime> colInicio;
    @FXML private TableColumn<ReservationsDto, LocalTime> colFin;
    @FXML private TableColumn<ReservationsDto, String> colEstado;
    @FXML private TableColumn<ReservationsDto, Void> colAcciones;

    private final ObservableList<ReservationsDto> reservas = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Mapear columnas a los getters existentes en ReservationsDto
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("coworkingSpaceId"));
        colFecha  .setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
        colInicio .setCellValueFactory(new PropertyValueFactory<>("startTime"));
        colFin    .setCellValueFactory(new PropertyValueFactory<>("endTime"));

        // Asignar la lista al TableView
        tablaReservas.setItems(reservas);

        // Pueden quedar colEspacio, colPiso, colEstado y colAcciones sin configurar de momento
    }

    @FXML
    private void onBuscar() {
        // TODO: implementar búsqueda filtrada
    }

    @FXML
    private void onLimpiar() {
        datePickerFiltro.getEditor().clear();
        comboUsuarios.getSelectionModel().clearSelection();
        comboPiso.getSelectionModel().clearSelection();
        reservas.clear();
    }

    @Override
    public void initialize() {
         // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
