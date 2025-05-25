package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.model.UsuariosDto;
import cr.ac.una.proyecto1progra2.service.UsuariosService;
import cr.ac.una.proyecto1progra2.util.FlowController;
import cr.ac.una.proyecto1progra2.util.Respuesta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class PrimaryController extends Controller {

    @FXML
    private TextField txtUsuario;
    @FXML
    private PasswordField txtContraseña;
    @FXML
    private Button btnInicioSesion;

    private final UsuariosService usuariosService = new UsuariosService();

    @Override
    public void initialize() {
        // Aquí podrías inicializar cosas si hiciera falta
    }

    @FXML
private void onActionBtnInicioSesion(ActionEvent event) {
    String usuario = txtUsuario.getText().trim();
    String contraseña = txtContraseña.getText().trim();

    if (usuario.isEmpty() || contraseña.isEmpty()) {
        mostrarError("Debe ingresar usuario y contraseña.");
        return;
    }

    Respuesta respuesta = usuariosService.getUsuario(usuario, contraseña);
    if (!respuesta.getEstado()) {
        mostrarError(respuesta.getMensaje());
        return;
    }

    UsuariosDto usuarioDto = (UsuariosDto) respuesta.getResultado("Usuario");

    // ✅ Verifica si el usuario está activo (true = activo)
    if (!usuarioDto.getEstado()) {
        mostrarError("El usuario está inactivo. Contacte al administrador.");
        return;
    }

    Long rolId = usuarioDto.getRolId();
    if (rolId != null) {
        FlowController.getInstance().goMain();
        getStage().close();
    } else {
        mostrarError("No se pudo determinar el rol del usuario.");
    }
}

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de inicio de sesión");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void onActionBtnRegistrarUsuario(ActionEvent event) {
        FlowController.getInstance().goViewInWindowModal("RegisterNewAccount", getStage(), true);
    }
}