package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.model.UsuariosDto;
import cr.ac.una.proyecto1progra2.service.UsuariosService;
import cr.ac.una.proyecto1progra2.util.FlowController;
import cr.ac.una.proyecto1progra2.util.Respuesta;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class EditUserController extends Controller {

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtPassword;

    private UsuariosDto usuario;
    private final UsuariosService usuariosService = new UsuariosService();

    @Override
    public void initialize() {
        // Inicialización si hace falta
    }

    public void setUsuario(UsuariosDto usuario) {
        this.usuario = usuario;
        cargarDatos();
    }

    private void cargarDatos() {
        if (usuario != null) {
            txtNombre.setText(usuario.getNombre());
            txtPassword.setText(usuario.getContraseña());
        } else {
            new Alert(Alert.AlertType.ERROR, "No hay usuario cargado para editar.").showAndWait();
        }
    }

    @FXML
    private void onGuardarCambios() {
        if (usuario == null) {
            new Alert(Alert.AlertType.ERROR, "No hay usuario para editar.").showAndWait();
            return;
        }

        if (!validarCampos()) {
            return;
        }

        usuario.setNombre(txtNombre.getText());
        usuario.setContraseña(txtPassword.getText().toLowerCase());

        Respuesta respuesta = usuariosService.guardarUsuario(usuario);
        if (respuesta.getEstado()) {
            new Alert(Alert.AlertType.INFORMATION, "Usuario actualizado correctamente.").showAndWait();

            // Volver a la tabla de usuarios
            FlowController.getInstance().limpiarLoader("EditDeleteUser");
            FlowController.getInstance().goViewInWindow("EditDeleteUser");
            getStage().close();
        } else {
            new Alert(Alert.AlertType.ERROR, "Error al actualizar el usuario.").showAndWait();
        }
    }

    @FXML
    private void volver() {
        FlowController.getInstance().limpiarLoader("EditDeleteUser");
        FlowController.getInstance().goViewInWindow("EditDeleteUser");
        getStage().close();
    }

    @FXML
    private void salir() {
        Platform.exit();
    }

    private boolean validarCampos() {
        if (txtNombre.getText().trim().isEmpty() || txtPassword.getText().trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Todos los campos deben estar completos.").showAndWait();
            return false;
        }
        return true;
    }
}
