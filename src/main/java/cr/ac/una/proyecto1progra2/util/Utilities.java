package cr.ac.una.proyecto1progra2.util;

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
}