package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.DTO.UsuariosDto;
import cr.ac.una.proyecto1progra2.service.UsuariosService;
import cr.ac.una.proyecto1progra2.util.Respuesta;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
        // inicializaciones si hiciera falta
    }

@FXML
private void onActionBtnCrearCuenta(ActionEvent event) {
    // 1) valida contraseñas
    if (!txtPassword.getText().equals(txtPasswordConfirm.getText())) {
        Alert err = new Alert(Alert.AlertType.ERROR);
        err.setTitle("Validación");
        err.setHeaderText(null);
        err.setContentText("Las contraseñas no coinciden.");
        err.showAndWait();
        return;
    }

    // 2) arma el DTO completando TODOS los campos no-null
    UsuariosDto dto = new UsuariosDto();
    dto.setUsername(txtUsername.getText().trim());
    dto.setCorreo(txtEmail.getText().trim());
    dto.setContraseña(txtPassword.getText());
    dto.setNombre(txtName.getText().trim());
    dto.setApellido(txtLastName.getText().trim());
    dto.setRolId(2L);  // rol por defecto

    // 3) llama al servicio y chequea la Respuesta
    Respuesta resp = usuariosService.guardarUsuario(dto);
    if (!resp.getEstado()) {
        Alert err = new Alert(Alert.AlertType.ERROR);
        err.setTitle("Error al guardar");
        err.setHeaderText(null);
        err.setContentText(resp.getMensaje());
        err.showAndWait();
        return;
    }

    // 4) extrae el DTO creado y muestra info
    UsuariosDto creado = (UsuariosDto) resp.getResultado("Usuario");
    Alert info = new Alert(Alert.AlertType.INFORMATION);
    info.setTitle("Éxito");
    info.setHeaderText(null);
    info.setContentText("Usuario guardado con ID: " + creado.getId());
    info.showAndWait();

    // 5) cierra la ventana sin volver a la principal (ya era modal)
    ((Stage) btnRegresar.getScene().getWindow()).close();
}

@FXML
private void onActionBtnRegresar(ActionEvent event) {
    Stage thisStage = (Stage) btnRegresar.getScene().getWindow();
    thisStage.close();
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