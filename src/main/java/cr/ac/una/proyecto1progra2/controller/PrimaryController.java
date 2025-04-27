// src/main/java/cr/ac/una/proyecto1progra2/controller/PrimaryController.java
package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.App;
import cr.ac.una.proyecto1progra2.service.UserService;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class PrimaryController implements Initializable {

    @FXML
    private Button btnInicioSecion;
    @FXML
    private TextField txtUsuario;
    @FXML
    private TextField txtContraseña;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // No hay inicialización adicional
    }

    @FXML
    private void switchToOptions(ActionEvent event) throws IOException {
        String usuario = txtUsuario.getText().trim();
        String contraseña = txtContraseña.getText().trim();

        // 1) Intento de validación en BD
        int role = UserService.validateLoginDB(usuario, contraseña);

        if (role == 1) {
            App.setRoot("optionsAdmin");
        } else if (role == 2) {
            App.setRoot("optionsUser");
        } else {
            // 2) Si falla, caemos al mock hard-code
            if (UserService.validateAdimin(usuario, contraseña)) {
                App.setRoot("optionsAdmin");
            } else if (UserService.validateUser(usuario, contraseña)) {
                App.setRoot("optionsUser");
            } else {
                // 3) Si todo falla, mostramos alerta
                mostrarError("Usuario o contraseña incorrectos.");
            }
        }
    }

    /** Muestra un Alert de tipo ERROR con mensaje dado */
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de inicio de sesión");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
