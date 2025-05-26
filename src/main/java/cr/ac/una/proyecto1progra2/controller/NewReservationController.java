package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.DTO.ReservationsDto;
import cr.ac.una.proyecto1progra2.service.ReservationsService;
import cr.ac.una.proyecto1progra2.util.FlowController;
import cr.ac.una.proyecto1progra2.util.Respuesta;
import cr.ac.una.proyecto1progra2.util.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

public class NewReservationController extends Controller implements Initializable {

    // ——— CONTROLES DE UI ———
    @FXML
    private TextField txtFirstName;    // antes usabas txtPiso
    @FXML
    private TextField txtLastName;     // nuevo: apellido
    @FXML
    private TextField txtSpaceId;      // antes quizás no lo tenías
    @FXML
    private TextField txtQuantity;     // antes txtDuracion?
    @FXML
    private DatePicker dpDate;         // antes txtFecha
    @FXML
    private TextField txtStartTime;    // antes txtHora
    @FXML
    private TextField txtEndTime;      // antes txtDuracion
    @FXML
    private TextField txtPrice;        // antes txtPrecio
    @FXML
    private Button btnSave;

    private final ReservationsService service = new ReservationsService();
private final ReservationsService reservationsService = new ReservationsService();
    @Override
    public void initialize() {
        // si necesitas cargar algo al inicio, hazlo aquí
    }

    @FXML
    private void onNewReservation(ActionEvent event) {
        FlowController.getInstance().goView("newReservation");
    }

    @FXML
private void onSave(ActionEvent event) {
    if (!validarFormulario()) return;

    try {
        ReservationsDto dto = new ReservationsDto();
        dto.setFirstName(txtFirstName.getText());
        dto.setLastName(txtLastName.getText());
        dto.setSpaceId(Long.valueOf(txtSpaceId.getText().trim()));
        dto.setQuantity(Integer.valueOf(txtQuantity.getText().trim()));
        dto.setDate(dpDate.getValue());
        dto.setStartTime(LocalTime.parse(txtStartTime.getText().trim()));
        dto.setEndTime(LocalTime.parse(txtEndTime.getText().trim()));
        dto.setPrice(new BigDecimal(txtPrice.getText().trim()));
        
        // 👉 Enlazar el usuario actual
        dto.setId(UserManager.getCurrentUser().getId());

        Respuesta r = reservationsService.guardarReserva(dto); // ✅ BIEN

        if (!r.getEstado()) {
            mostrarError(r.getMensaje());
            return;
        }

        ReservationsDto saved = (ReservationsDto) r.getResultado("Reserva");
        loadIntoForm(saved);
        mostrarInfo("Reserva guardada correctamente.");
    } catch (Exception ex) {
        mostrarError("Error al guardar la reserva: " + ex.getMessage());
    }
}

    private boolean validarFormulario() {
    if (txtFirstName.getText().isBlank() ||
        txtLastName.getText().isBlank() ||
        txtSpaceId.getText().isBlank() ||
        dpDate.getValue() == null ||
        txtStartTime.getText().isBlank() ||
        txtEndTime.getText().isBlank() ||
        txtQuantity.getText().isBlank() ||
        txtPrice.getText().isBlank()) {
        mostrarError("Todos los campos son obligatorios.");
        return false;
    }

    try {
        Integer.parseInt(txtQuantity.getText());
        new BigDecimal(txtPrice.getText());
        LocalTime.parse(txtStartTime.getText());
        LocalTime.parse(txtEndTime.getText());
    } catch (Exception e) {
        mostrarError("Formato inválido en cantidad, precio o tiempo.");
        return false;
    }

    return true;
}

    // Método auxiliar para volcar datos de DTO a la UI:
    private void loadIntoForm(ReservationsDto reserva) {
        txtFirstName.setText(reserva.getFirstName());                       // CAMBIO: antes getFloorName()
        txtLastName.setText(reserva.getLastName());                         // NUEVO
        txtSpaceId.setText(reserva.getSpaceId().toString());                // CAMBIO
        txtQuantity.setText(reserva.getQuantity().toString());              // CAMBIO
        dpDate.setValue(reserva.getDate());                                 // CAMBIO: antes txtFecha.setText(...)
        txtStartTime.setText(reserva.getStartTime().toString());            // CAMBIO: antes getTime()
        txtEndTime.setText(reserva.getEndTime().toString());                // CAMBIO: antes getDuration()
        txtPrice.setText(reserva.getPrice().toPlainString());               // CAMBIO
    }

    private void mostrarError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }

    private void mostrarInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

}
