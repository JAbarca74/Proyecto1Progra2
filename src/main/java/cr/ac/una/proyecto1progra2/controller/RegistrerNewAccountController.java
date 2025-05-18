package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.model.UsuariosDto;
import cr.ac.una.proyecto1progra2.service.UsuariosService;
import cr.ac.una.proyecto1progra2.util.FlowController;
import cr.ac.una.proyecto1progra2.util.Respuesta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegistrerNewAccountController extends Controller {

    @FXML
    private Button btnRegresar;
    @FXML
    private Button btnCrearCuenta;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtAge;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtPasswordConfirm;
    @FXML
    private CheckBox chkAdmin;
    @FXML
    private CheckBox chkUser;

    private final UsuariosService usuariosService = new UsuariosService();

    @Override
    public void initialize() {
       
        chkAdmin.setOnAction(e -> {
            if (chkAdmin.isSelected()) chkUser.setSelected(false);
        });
        chkUser.setOnAction(e -> {
            if (chkUser.isSelected()) chkAdmin.setSelected(false);
        });
    }

    @FXML
    private void onActionBtnRegresar(ActionEvent event) {
        FlowController.getInstance().goView("userManager");
    }

    @FXML
    private void onActionBtnCrearCuenta(ActionEvent event) {
        String name     = txtName.getText().trim();
        String id       = txtId.getText().trim();
        String age      = txtAge.getText().trim();
        String username = txtUsername.getText().trim();
        String pass     = txtPassword.getText();
        String confirm  = txtPasswordConfirm.getText();

        // Validaciones básicas
        if (name.isEmpty() || id.isEmpty() || age.isEmpty()
            || username.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            mostrarError("Debe completar todos los campos.");
            return;
        }
        if (!pass.equals(confirm)) {
            mostrarError("Las contraseñas no coinciden.");
            return;
        }
        if (!chkAdmin.isSelected() && !chkUser.isSelected()) {
            mostrarError("Debe seleccionar un rol.");
            return;
        }

        // Construir DTO y guardar
        UsuariosDto dto = new UsuariosDto();
        dto.setNombre(username);
        dto.setContraseña(pass);
        dto.setRolId(chkAdmin.isSelected() ? 1L : 2L);
        dto.setEstado("A");

        Respuesta resp = usuariosService.guardarUsuario(dto);
        if (resp.getEstado()) {
            mostrarInfo("Cuenta creada exitosamente.");
          
        } else {
            mostrarError(resp.getMensaje());
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error al crear cuenta");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInfo(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registro exitoso");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
