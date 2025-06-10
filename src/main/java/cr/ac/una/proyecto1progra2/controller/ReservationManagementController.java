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
import javafx.geometry.Insets;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonType;
import java.time.LocalTime;
import javafx.scene.control.ComboBox;


import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.layout.HBox;

public class ReservationManagementController extends Controller implements Initializable {

    @FXML private Label lblMonthYear;
    @FXML private GridPane calendarGrid;

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
    private List<ReservationViewDto> allReservas; 

    @Override
    public void initialize(URL loc, ResourceBundle rb) {
        colUsuario.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getUserName()));
        colEspacio.setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getSpaceName()));
        colPiso   .setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getPiso()));
        colFecha  .setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
        colInicio .setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getStartTime().toString()));
        colFin    .setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getEndTime().toString()));
        colEstado .setCellValueFactory(c -> new ReadOnlyStringWrapper(c.getValue().getEstado()));
        colAcciones.setCellFactory(col -> new TableCell<>() {
        private final Button btnEditar  = new Button("Editar");
        private final Button btnEliminar = new Button("Eliminar");
        private final HBox   box        = new HBox(5, btnEditar, btnEliminar);

    {
       
        btnEditar.getStyleClass().add("boton-pequeno");
        btnEliminar.getStyleClass().add("boton-pequeno");


        btnEliminar.setOnAction(e -> {
            ReservationViewDto dto = getTableView().getItems().get(getIndex());
            if (reservationsService.eliminarReserva(dto.getId())) {
                reservas.remove(dto);
                drawCalendar();
            } else {
                new Alert(Alert.AlertType.ERROR, "No se pudo eliminar").showAndWait();
            }
        });

        // Acción EDITAR: abre un diálogo sencillo para nueva fecha/hora
        btnEditar.setOnAction(e -> {
            ReservationViewDto dto = getTableView().getItems().get(getIndex());
            showEditDialog(dto);
        });
    }

    @Override protected void updateItem(Void v, boolean empty) {
        super.updateItem(v, empty);
        setGraphic(empty ? null : box);
    }
});

        tablaReservas.setItems(reservas);

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

        comboPiso.setItems(pisos);

        cargarUsuarios();
        allReservas = reservationsService.listarTodasView();
        reservas.setAll(allReservas);

        currentYearMonth = YearMonth.now();
        drawCalendar();
    }

    private void cargarUsuarios(){
        Respuesta r = usuariosService.listarUsuarios();
        if(r.isSuccess()){
            usuarios.setAll((List<UsuariosDto>)r.getResultado("Usuarios"));
            comboUsuarios.setItems(usuarios);
        }
    }


    private void drawCalendar() {
    lblMonthYear.setText(currentYearMonth.getMonth().name() + " " + currentYearMonth.getYear());

    calendarGrid.getChildren().removeIf(node ->
        GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) > 0
    );

    LocalDate first = currentYearMonth.atDay(1);
    int offset = (first.getDayOfWeek().getValue() + 6) % 7; 
    int days = currentYearMonth.lengthOfMonth();

    for (int d = 1; d <= days; d++) {
        LocalDate date = currentYearMonth.atDay(d);
        int col = (offset + d - 1) % 7;
        int row = 1 + (offset + d - 1) / 7;

        Button b = new Button(String.valueOf(d));
        b.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        boolean has = allReservas.stream()
                        .anyMatch(r -> r.getReservationDate() != null && r.getReservationDate().equals(date));
        if (has) {
            b.setStyle("-fx-background-color:#90ee90;");
        }
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
    
    
   


private void showEditDialog(ReservationViewDto dto) {
    
    DatePicker dp = new DatePicker(dto.getReservationDate());
    ComboBox<LocalTime> cbInicio = new ComboBox<>(generarHoras());
    ComboBox<LocalTime> cbFin    = new ComboBox<>(generarHoras());

    cbInicio.setValue(dto.getStartTime());
    cbFin   .setValue(dto.getEndTime());
    
    
cbInicio.valueProperty().addListener((obs, oldTime, newTime) -> {
    if (newTime != null) {
        LocalTime siguiente = newTime.plusMinutes(30);
        
        if (cbFin.getItems().contains(siguiente)) {
            cbFin.setValue(siguiente);
        }
    }
});

// Evitar que fin sea menor que inicio+30min
cbFin.valueProperty().addListener((obs, oldVal, newVal) -> {
    LocalTime inicio = cbInicio.getValue();
    if (newVal != null && (inicio == null || newVal.isBefore(inicio.plusMinutes(30)))) {
        // forzamos fin = inicio + 30min
        cbFin.setValue(inicio.plusMinutes(30));
    }
});


    
  
    Dialog<ButtonType> dialog = new Dialog<>();
    dialog.setTitle("Editar Reserva");
    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

    GridPane grid = new GridPane();
    grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(20));
    grid.add(new Label("Fecha:"), 0, 0);
    grid.add(dp, 1, 0);
    grid.add(new Label("Inicio:"), 0, 1);
    grid.add(cbInicio, 1, 1);
    grid.add(new Label("Fin:"), 0, 2);
    grid.add(cbFin,    1, 2);

    dialog.getDialogPane().setContent(grid);

    // 3) Procesar resultado
    dialog.showAndWait().ifPresent(res -> {
    if (res == ButtonType.OK) {
        LocalDate newDate  = dp.getValue();
        LocalTime newStart = cbInicio.getValue();
        LocalTime newEnd   = cbFin   .getValue();

        
        if (reservationsService.hayColision(dto.getId(), newDate, newStart, newEnd)) {
            new Alert(Alert.AlertType.WARNING,
                "Ya existe otra reserva ese día/hora").showAndWait();
            return;  
        }

        
        boolean ok = reservationsService.actualizarReserva(
                        dto.getId(), newDate, newStart, newEnd);
        if (ok) {
    
    allReservas = reservationsService.listarPorFiltros(
        null,
        comboUsuarios.getValue() != null ? comboUsuarios.getValue().getId() : null,
        comboPiso.getValue()      != null ? comboPiso.getValue()       : ""
    );
    reservas.setAll(allReservas);

    // 2) actualizar el calendario en tiempo real
    drawCalendar();
} else {
    new Alert(Alert.AlertType.ERROR, "No se pudo actualizar").showAndWait();
}

    }
});
}


private ObservableList<LocalTime> generarHoras() {
    ObservableList<LocalTime> horas = FXCollections.observableArrayList();
    LocalTime t = LocalTime.of(7, 0);
    LocalTime fin = LocalTime.of(21, 0);
    while (!t.isAfter(fin)) {
        horas.add(t);
        t = t.plusMinutes(30);
    }
    return horas;
}


    
    @FXML private void onPrevMonth(ActionEvent ev){
        currentYearMonth = currentYearMonth.minusMonths(1);
        drawCalendar();
    }

    @FXML private void onNextMonth(ActionEvent ev){
        currentYearMonth = currentYearMonth.plusMonths(1);
        drawCalendar();
    }


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
