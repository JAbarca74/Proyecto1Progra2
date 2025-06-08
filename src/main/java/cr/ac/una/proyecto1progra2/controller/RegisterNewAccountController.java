package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.DTO.UsuariosDto;
import cr.ac.una.proyecto1progra2.service.UsuariosService;
import cr.ac.una.proyecto1progra2.util.FlowController;
import cr.ac.una.proyecto1progra2.util.Respuesta;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegisterNewAccountController extends Controller implements Initializable {

     @FXML private TextField txtUsername;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtPasswordConfirm;
    @FXML private TextField txtName;
    @FXML private TextField txtLastName;      // <-- asegúrate de tener este fx:id en el FXML
    @FXML private Button btnCrearCuenta;
    @FXML private Button btnRegresar;

    // inyectamos el servicio
    private final UsuariosService usuariosService = new UsuariosService();

    @FXML
    public void initialize() {
     btnCrearCuenta.disableProperty().bind(
    txtUsername.textProperty().isEmpty()
        .or(txtEmail.textProperty().isEmpty())
        .or(txtPassword.textProperty().isEmpty())
        .or(txtPasswordConfirm.textProperty().isEmpty())
        .or(txtName.textProperty().isEmpty())
        .or(txtLastName.textProperty().isEmpty())
     );
    }
    
    @FXML
private void onActionBtnCrearCuenta(ActionEvent event) {
    boolean invalido = false;

    limpiarEstilos(); // Limpia clases "invalid" previas

    String username = txtUsername.getText().trim();
    String email = txtEmail.getText().trim();
    String password = txtPassword.getText();
    String confirmPassword = txtPasswordConfirm.getText();
    String nombre = txtName.getText().trim();
    String apellido = txtLastName.getText().trim();

    // Validaciones:
    invalido |= marcarInvalido(txtUsername, username.isEmpty());

    invalido |= marcarInvalido(txtName,
        nombre.isEmpty() || !nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+"));

    invalido |= marcarInvalido(txtLastName,
        apellido.isEmpty() || !apellido.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+"));

    invalido |= marcarInvalido(txtEmail,
        email.isEmpty() || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"));

    invalido |= marcarInvalido(txtPassword,
        password.isEmpty() || password.length() <= 4);

    invalido |= marcarInvalido(txtPasswordConfirm,
        !password.equals(confirmPassword));

    if (invalido) {
        mostrarAlerta("Por favor corrige los campos resaltados en rojo.");
        return;
    }

    // Crear el DTO si todo es válido
    UsuariosDto dto = new UsuariosDto();
    dto.setUsername(username);
    dto.setCorreo(email);
    dto.setContraseña(password);
    dto.setNombre(nombre);
    dto.setApellido(apellido);
    dto.setRolId(2L); // Rol de usuario por defecto

    Respuesta resp = usuariosService.guardarUsuario(dto);
    if (!resp.getEstado()) {
        mostrarAlerta("Error al guardar: " + resp.getMensaje());
        return;
    }

    mostrarInfo("¡Usuario creado correctamente!");
    
     limpiarFormulario();
    
 

}

@FXML
private void onActionBtnRegresar(ActionEvent event) {
    boolean camposLlenos =
        !txtUsername.getText().trim().isEmpty() ||
        !txtEmail.getText().trim().isEmpty() ||
        !txtPassword.getText().isEmpty() ||
        !txtPasswordConfirm.getText().isEmpty() ||
        !txtName.getText().trim().isEmpty() ||
        !txtLastName.getText().trim().isEmpty();

    if (camposLlenos) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmar regreso");
        confirm.setHeaderText("¿Cancelar creación del usuario?");
        confirm.setContentText("Perderás los datos ingresados.");

        ButtonType si = new ButtonType("Sí");
        ButtonType no = new ButtonType("No");
        confirm.getButtonTypes().setAll(si, no);

        if (confirm.showAndWait().orElse(no) != si) {
            return;
        }
    }

        limpiarFormulario();
    
 Stage thisStage = (Stage) btnRegresar.getScene().getWindow();
    thisStage.close();
    

}

private void limpiarFormulario() {
    txtUsername.clear();
    txtEmail.clear();
    txtPassword.clear();
    txtPasswordConfirm.clear();
    txtName.clear();
    txtLastName.clear();
    limpiarEstilos();
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

private void limpiarEstilos() {
    txtUsername.getStyleClass().remove("invalid");
    txtEmail.getStyleClass().remove("invalid");
    txtPassword.getStyleClass().remove("invalid");
    txtPasswordConfirm.getStyleClass().remove("invalid");
    txtName.getStyleClass().remove("invalid");
    txtLastName.getStyleClass().remove("invalid");
}

private void mostrarAlerta(String mensaje) {
    Alert alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle("Validación");
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


private boolean validarFormulario() {
    boolean hayError = false;

    // Limpia estilos anteriores
    limpiarEstilos(txtUsername, txtEmail, txtPassword, txtPasswordConfirm, txtName, txtLastName);

    if (txtUsername.getText().trim().isEmpty()) {
        marcarInvalido(txtUsername);
        hayError = true;
    }

    String email = txtEmail.getText().trim();
    if (email.isEmpty() || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
        marcarInvalido(txtEmail);
        hayError = true;
    }

    String password = txtPassword.getText();
    if (password.isEmpty() || password.length() < 6) {
        marcarInvalido(txtPassword);
        hayError = true;
    }

    if (!password.equals(txtPasswordConfirm.getText())) {
        marcarInvalido(txtPasswordConfirm);
        hayError = true;
    }

    if (!txtName.getText().trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
        marcarInvalido(txtName);
        hayError = true;
    }

    if (!txtLastName.getText().trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
        marcarInvalido(txtLastName);
        hayError = true;
    }

    return !hayError;
}

private void marcarInvalido(Control campo) {
    if (!campo.getStyleClass().contains("invalid")) {
        campo.getStyleClass().add("invalid");
    }
}

private void limpiarEstilos(Control... campos) {
    for (Control campo : campos) {
        campo.getStyleClass().remove("invalid");
    }
}


private boolean marcarInvalido(TextField campo, boolean condicion) {
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




   private void closeWindow() {
    Stage s = (Stage) btnRegresar.getScene().getWindow();
    s.close();
}


    private void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    private void showInfo(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }
    
 

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
}