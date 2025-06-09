package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.DTO.UsuariosDto;
import cr.ac.una.proyecto1progra2.service.UsuariosService;
import cr.ac.una.proyecto1progra2.util.Respuesta;
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
 

    @FXML@Override
 public void initialize() {
    }

    @FXML

private void onActionBtnCrearCuenta(ActionEvent event) {
    // Verificar que las contraseñas coincidan
    if (!txtPassword.getText().equals(txtPasswordConfirm.getText())) {
        new Alert(Alert.AlertType.ERROR, "Las contraseñas no coinciden").show();
        return;
    }

    try {
        // Construir el DTO
        UsuariosDto dto = new UsuariosDto();
        dto.setUsername(txtUsername.getText().trim());
        dto.setCorreo(txtEmail.getText().trim());
        dto.setContraseña(txtPassword.getText().trim());
        dto.setNombre(txtName.getText().trim());
        dto.setApellido(txtLastName.getText().trim());
        dto.setRolId(1L);       // Rol ADMIN
        dto.setEstado(true);    // Activo

        // Guardar usuario (sin usar clase Respuesta)
        UsuariosService userService = new UsuariosService();
        userService.guardarUsuario(dto);  // Si lanza error, será capturado

        new Alert(Alert.AlertType.INFORMATION, "Administrador creado con éxito").show();

        // Limpiar campos
        txtUsername.clear();
        txtEmail.clear();
        txtPassword.clear();
        txtPasswordConfirm.clear();
        txtName.clear();
        txtLastName.clear();

    } catch (Exception ex) {
        ex.printStackTrace();
        new Alert(Alert.AlertType.ERROR, "Error al crear administrador:\n" + ex.getMessage()).show();
    }
}


private void limpiarFormulario() {
    txtUsername.clear();
    txtEmail.clear();
    txtPassword.clear();
    txtPasswordConfirm.clear();
    txtName.clear();
    txtLastName.clear();
}


 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
}