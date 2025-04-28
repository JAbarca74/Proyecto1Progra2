package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.model.UsuariosDto;
import cr.ac.una.proyecto1progra2.service.UsuariosService;
import cr.ac.una.proyecto1progra2.util.FlowController;
import cr.ac.una.proyecto1progra2.util.Respuesta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class PrimaryController extends Controller {

    @FXML
    private Button btnInicioSesion;
    @FXML
    private TextField txtUsuario;
    @FXML
    private TextField txtContraseña;

    private final UsuariosService usuariosService = new UsuariosService();

    @Override
    public void initialize() {
        // Aquí podrías inicializar cosas si quieres, por ejemplo, limpiar campos, poner focus, etc.
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

        if (respuesta.getEstado()) {
            UsuariosDto usuarioDto = (UsuariosDto) respuesta.getResultado("Usuario");

            // Dependiendo del rol, cambia la vista
            if (usuarioDto.getRolId() != null) {
                if (usuarioDto.getRolId() == 1) { // Rol Administrador
                    FlowController.getInstance().goView("optionsAdmin");
                } else if (usuarioDto.getRolId() == 2) { // Rol Usuario
                    FlowController.getInstance().goView("optionsUser");
                } else {
                    mostrarError("Rol de usuario desconocido.");
                }
            } else {
                mostrarError("No se pudo determinar el rol del usuario.");
            }
        } else {
            mostrarError(respuesta.getMensaje());
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de inicio de sesión");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
