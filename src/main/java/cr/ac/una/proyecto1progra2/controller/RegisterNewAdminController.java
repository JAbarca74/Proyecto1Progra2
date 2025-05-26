package cr.ac.una.proyecto1progra2.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterNewAdminController extends Controller implements Initializable{

    @FXML private TextField txtUsername;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtPasswordConfirm;
    @FXML private TextField txtName;
    @FXML private TextField txtLastName;
    @FXML private Button btnCrearCuenta;
    @FXML private Button btnRegresar;

    @FXML public void initialize() {
    }

    @FXML private void onActionBtnCrearCuenta(ActionEvent event) {
        if (!txtPassword.getText().equals(txtPasswordConfirm.getText())) {
            new Alert(Alert.AlertType.ERROR, "Las contraseñas no coinciden").show();
            return;
        }
        // TODO: persiste administrador con rol ADMIN
        new Alert(Alert.AlertType.INFORMATION, "Administrador creado con éxito").show();
        btnRegresar.getScene().getWindow().hide();
    }

    @FXML private void onActionBtnRegresar(ActionEvent event) {
        btnRegresar.getScene().getWindow().hide();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
}