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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.ResourceBundle;

public class ReservationManagementController extends Controller implements Initializable {

    // Calendario
    @FXML private Label lblMonthYear;
    @FXML private GridPane calendarGrid;

    // Filtros / tabla
    @FXML private ComboBox<UsuariosDto> comboUsuarios;
    @FXML private ComboBox<String> comboPiso;
    @FXML private TableView<ReservationViewDto> tablaReservas;
    @FXML private TableColumn<ReservationViewDto,String> colUsuario;
    @FXML private TableColumn<ReservationViewDto,String> colEspacio;
    @FXML private TableColumn<ReservationViewDto,String> colPiso;
    @FXML private TableColumn<ReservationViewDto,LocalDate> colFecha;
    @FXML private TableColumn<ReservationViewDto,String> colInicio;
    @FXML private TableColumn<ReservationViewDto,String> colFin;
    @FXML private TableColumn<ReservationViewDto,String> colEstado;
    @FXML private TableColumn<ReservationViewDto,Void> colAcciones;

    private final ReservationsService   reservationsService = new ReservationsService();
    private final UsuariosService       usuariosService     = new UsuariosService();
    private final ObservableList<ReservationViewDto> reservas = FXCollections.observableArrayList();
    private final ObservableList<UsuariosDto>         usuarios = FXCollections.observableArrayList();
    private final ObservableList<String>              pisos    = FXCollections.observableArrayList("P0","P1","P2","P3");

    private YearMonth currentYearMonth;
    private List<ReservationViewDto> allReservas;  // cache completo

    @Override
    public void initialize(URL loc, ResourceBundle rb) {
        // Inicializar columnas de la tabla
        colUsuario.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getUserName()));
        colEspacio.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getSpaceName()));
        colPiso   .setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getPiso()));
        colFecha  .setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
        colInicio .setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getStartTime().toString()));
        colFin    .setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getEndTime().toString()));
        colEstado .setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getEstado()));
        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Eliminar");
            {
                btn.setOnAction(e->{
                    ReservationViewDto dto = getTableView().getItems().get(getIndex());
                    if (reservationsService.eliminarReserva(dto.getId())) {
                        reservas.remove(dto);
                        drawCalendar(); // calendario en tiempo real
                    } else {
                        new Alert(Alert.AlertType.ERROR, "No se pudo eliminar").showAndWait();
                    }
                });
            }
            @Override protected void updateItem(Void v, boolean empty){
                super.updateItem(v, empty);
                setGraphic(empty?null:btn);
            }
        });
        tablaReservas.setItems(reservas);

        // Inicializar comboUsuarios
        comboUsuarios.setConverter(new StringConverter<UsuariosDto>() {
            @Override public String toString(UsuariosDto u){ return u!=null?u.getUsername():""; }
            @Override public UsuariosDto fromString(String s){ return null; }
        });
        comboUsuarios.setCellFactory(lv->new ListCell<>() {
            @Override protected void updateItem(UsuariosDto u, boolean empty){
                super.updateItem(u,empty);
                setText(empty||u==null?"":u.getUsername());
            }
        });

        // Inicializar comboPiso
        comboPiso.setItems(pisos);

        // Cargar datos
        cargarUsuarios();
        allReservas = reservationsService.listarTodasView();
        reservas.setAll(allReservas);

        // Configurar calendario
        currentYearMonth = YearMonth.now();
        drawCalendar();
    }

    private void cargarUsuarios(){
        Respuesta r = usuariosService.listarUsuarios();
        if(r.isSuccess()){
            //noinspection unchecked
            usuarios.setAll((List<UsuariosDto>)r.getResultado("Usuarios"));
            comboUsuarios.setItems(usuarios);
        }
    }

    // -------------------- calendario --------------------

    private void drawCalendar() {
    // 1) recarga todas las reservas (esto parece pendiente, asegurate de que allReservas esté actualizado)

    // 3) pintar mes/año
    lblMonthYear.setText(currentYearMonth.getMonth().name() + " " + currentYearMonth.getYear());

    // 4) limpiar días viejos
    calendarGrid.getChildren().removeIf(node ->
        GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0
    );

    LocalDate first = currentYearMonth.atDay(1);
    int offset = (first.getDayOfWeek().getValue() + 6) % 7; // lunes=0
    int days = currentYearMonth.lengthOfMonth();

    for (int d = 1; d <= days; d++) {
        LocalDate date = currentYearMonth.atDay(d);
        int col = (offset + d - 1) % 7;
        int row = 1 + (offset + d - 1) / 7;

        Button b = new Button(String.valueOf(d));
        b.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        // colorear si hay reservas ese día, validando null
        boolean has = allReservas.stream()
                        .anyMatch(r -> r.getReservationDate() != null && r.getReservationDate().equals(date));
        if (has) {
            b.setStyle("-fx-background-color:#90ee90;");
        }

        // al click, filtrar por fecha
        b.setOnAction(evt -> {
            reservas.setAll(
                reservationsService.listarPorFiltros(
                    date,
                    comboUsuarios.getValue() != null ? comboUsuarios.getValue().getId() : null,
                    comboPiso.getValue() != null ? comboPiso.getValue() : ""
                )
            );
        });

        calendarGrid.add(b, col, row);
    }
}
    @FXML private void onPrevMonth(ActionEvent ev){
        currentYearMonth = currentYearMonth.minusMonths(1);
        drawCalendar();
    }

    @FXML private void onNextMonth(ActionEvent ev){
        currentYearMonth = currentYearMonth.plusMonths(1);
        drawCalendar();
    }

    // -------------------- filtros y recarga tabla --------------------

    @FXML private void onBuscar(ActionEvent ev) {
        reservas.setAll(
            reservationsService.listarPorFiltros(
                null,
                comboUsuarios.getValue() != null ? comboUsuarios.getValue().getId() : null,
                comboPiso.getValue() != null ? comboPiso.getValue() : ""
            )
        );
        drawCalendar();
    }

    @FXML private void onLimpiar(ActionEvent ev) {
        comboUsuarios.getSelectionModel().clearSelection();
        comboPiso  .getSelectionModel().clearSelection();
        reservas.setAll(allReservas);
        drawCalendar();
    }

    @Override
    public void initialize() {
        
    }
}
