package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.DTO.ReservationViewDto;
import cr.ac.una.proyecto1progra2.service.ReservationsService;
import cr.ac.una.proyecto1progra2.util.UserManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Muestra las reservas del usuario actual y permite cancelarlas.
 */
public class MisReservasViewController extends Controller implements Initializable {

    @FXML private ListView<ReservationViewDto> lvReservas;

    private final ReservationsService svc = new ReservationsService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarReservas();
    }

    @Override public void initialize() { /* requerido por Controller */ }

    private void cargarReservas() {
        long uid = UserManager.getCurrentUser().getId();
        var items = svc.listarPorFiltros(null, uid, null);
        lvReservas.getItems().setAll(items);
        lvReservas.setCellFactory(new Callback<>() {
            @Override
            public ListCell<ReservationViewDto> call(ListView<ReservationViewDto> list) {
                return new ListCell<>() {
                    private final Text txt = new Text();
                    private final Button btn = new Button("Cancelar");
                    private final HBox box = new HBox(txt, btn);
                    {
                        HBox.setHgrow(txt, Priority.ALWAYS);
                        box.setSpacing(10);
                        btn.getStyleClass().add("boton-cerrar-sesion");
                        btn.setOnAction(e -> {
                            var dto = getItem();
                            if (dto == null) return;
                            if (svc.eliminarReserva(dto.getId())) {
                                getListView().getItems().remove(dto);
                            } else {
                                new Alert(Alert.AlertType.ERROR,
                                          "Error al cancelar reserva")
                                    .showAndWait();
                            }
                        });
                    }
                    @Override
                    protected void updateItem(ReservationViewDto dto, boolean empty) {
                        super.updateItem(dto, empty);
                        if (empty || dto == null) {
                            setGraphic(null);
                        } else {
                            String fecha = dto.getReservationDate()
                                              .format(DateTimeFormatter.ofPattern("d MMM yyyy"));
                            txt.setText(dto.getSpaceName()
                                + " • " + fecha
                                + "  " + dto.getStartTime()
                                + "-" + dto.getEndTime());
                            setGraphic(box);
                        }
                    }
                };
            }
        });
    }
}
