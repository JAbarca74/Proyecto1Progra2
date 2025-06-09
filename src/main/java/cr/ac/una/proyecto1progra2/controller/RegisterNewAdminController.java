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
import javafx.scene.control.Control;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;


public class RegisterNewAdminController extends Controller implements Initializable{

    @FXML private TextField txtUsername;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtPasswordConfirm;
    @FXML private TextField txtName;
    @FXML private TextField txtLastName;
    @FXML private Button btnCrearCuenta;
    @FXML private RadioButton rbUsuario;
@FXML private RadioButton rbAdmin;
private ToggleGroup rolGroup;

 

    private final UsuariosService usuariosService = new UsuariosService();

    @FXML public void initialize() {
    }

    @FXML
    private void onActionBtnCrearCuenta(ActionEvent event) {
        boolean invalido = false;
        limpiarEstilos();

        String username        = txtUsername.getText().trim();
        String email           = txtEmail.getText().trim();
        String password        = txtPassword.getText();
        String confirmPassword = txtPasswordConfirm.getText();
        String nombre          = txtName.getText().trim();
        String apellido        = txtLastName.getText().trim();

        // Validaciones de campos
        invalido |= marcarInvalido(txtUsername, username.isEmpty());
        invalido |= marcarInvalido(txtName, nombre.isEmpty() || !nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+"));
        invalido |= marcarInvalido(txtLastName, apellido.isEmpty() || !apellido.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+"));
        invalido |= marcarInvalido(txtEmail, email.isEmpty() || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"));
        invalido |= marcarInvalido(txtPassword, password.isEmpty() || password.length() <= 4);
        invalido |= marcarInvalido(txtPasswordConfirm, !password.equals(confirmPassword));

        if (invalido) {
            mostrarAlerta("Por favor corrige los campos resaltados en rojo.");
            return;
        }

        // --- VALIDACIÓN DE SELECCIÓN DE ROL (opcional) ---
        if (rolGroup.getSelectedToggle() == null) {
            mostrarAlerta("Por favor seleccioná Tipo: Usuario o Administrador.");
            return;
        }
        // ---------------------------------------------------

        // --- DETERMINAR EL rolId SEGÚN EL RADIO BUTTON SELECCIONADO ---
        Long rolId = rbAdmin.isSelected() ? 1L : 2L;
        // (1L = administrador, 2L = usuario; ajustá a tus IDs reales)
        // ----------------------------------------------------------------

        // Construcción del DTO
        UsuariosDto dto = new UsuariosDto();
        dto.setUsername(username);
        dto.setCorreo(email);
        dto.setContraseña(password);
        dto.setNombre(nombre);
        dto.setApellido(apellido);
        dto.setRolId(rolId);

        // Guardar en la base de datos
        Respuesta resp = usuariosService.guardarUsuario(dto);
        if (!resp.getEstado()) {
            mostrarAlerta("Error al guardar: " + resp.getMensaje());
            return;
        }

        mostrarInfo("¡Cuenta creada correctamente!");
        limpiarFormulario();
    }
 private void limpiarFormulario() {
        txtUsername.clear();
        txtEmail.clear();
        txtPassword.clear();
        txtPasswordConfirm.clear();
        txtName.clear();
        txtLastName.clear();
        limpiarEstilos();
        // Reiniciar selector a Usuario
        rbUsuario.setSelected(true);
    }

    private void limpiarEstilos() {
        txtUsername.getStyleClass().remove("invalid");
        txtEmail.getStyleClass().remove("invalid");
        txtPassword.getStyleClass().remove("invalid");
        txtPasswordConfirm.getStyleClass().remove("invalid");
        txtName.getStyleClass().remove("invalid");
        txtLastName.getStyleClass().remove("invalid");
    }

    private boolean marcarInvalido(Control campo, boolean condicion) {
        if (condicion) {
            if (!campo.getStyleClass().contains("invalid")) {
                campo.getStyleClass().add("invalid");
            }
            return true;
        } else {
            campo.getStyleClass().remove("invalid");
            return false;
        }
    }

    private void mostrarAlerta(String mensaje) {
        new Alert(Alert.AlertType.WARNING, mensaje).showAndWait();
    }

    private void mostrarInfo(String mensaje) {
        new Alert(Alert.AlertType.INFORMATION, mensaje).showAndWait();
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 1) Deshabilitar botón Crear hasta que todos los campos estén llenos
        btnCrearCuenta.disableProperty().bind(
            txtUsername.textProperty().isEmpty()
          .or(txtEmail.textProperty().isEmpty())
          .or(txtPassword.textProperty().isEmpty())
          .or(txtPasswordConfirm.textProperty().isEmpty())
          .or(txtName.textProperty().isEmpty())
          .or(txtLastName.textProperty().isEmpty())
        );
        
        // 2) Configurar el ToggleGroup para los RadioButtons de rol
        rolGroup = new ToggleGroup();
        rbUsuario.setToggleGroup(rolGroup);
        rbAdmin.setToggleGroup(rolGroup);
        rbUsuario.setSelected(true); // Por defecto "Usuario"
    }
}