package cr.ac.una.proyecto1progra2.util;

import cr.ac.una.proyecto1progra2.controller.NewReservationController;
import java.lang.ModuleLayer.Controller;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Utilities {

    public static void showAlert(AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    public static void mostrarMensaje(String titulo, String mensaje) {
    showAlert(AlertType.INFORMATION, titulo, mensaje);
}
    
    
    public static NewReservationController getNewReservationControllerIfActive() {
    try {
        return (NewReservationController) FlowController.getInstance().getController("NewReservation");
    } catch (Exception e) {
        return null;
    }
}
}