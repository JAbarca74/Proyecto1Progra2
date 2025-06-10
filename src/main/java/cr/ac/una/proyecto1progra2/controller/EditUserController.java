package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.DTO.UsuariosDto;
import cr.ac.una.proyecto1progra2.service.UsuariosService;
import cr.ac.una.proyecto1progra2.util.FlowController;
import cr.ac.una.proyecto1progra2.util.Respuesta;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class EditUserController extends Controller {

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtUsername;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtNuevaPassword;
    @FXML private TextField txtConfirmPassword;
    @FXML private Button btnEstado;
    private UsuariosDto usuario;
    private final UsuariosService usuariosService = new UsuariosService();
private boolean activo = true;
    @Override
    public void initialize() {
       
    }

    public void setUsuario(UsuariosDto u) {
        this.usuario = u;
        cargarDatos();
    }
@FXML
private void onCambiarEstado() {
    activo = !activo;
    usuario.setEstado(activo ? "A" : "I");
    btnEstado.setText(activo ? "Activo" : "Inactivo");
    btnEstado.setStyle(activo
        ? "-fx-background-color: green; -fx-text-fill: white;"
        : "-fx-background-color: red; -fx-text-fill: white;");
}
    private void cargarDatos() {
        if (usuario == null) {
            new Alert(Alert.AlertType.ERROR, "No hay usuario cargado para editar.").showAndWait();
            return;
        }
        txtNombre.setText(usuario.getNombre());
        txtApellido.setText(usuario.getApellido());
        txtUsername.setText(usuario.getUsername());
        txtCorreo.setText(usuario.getCorreo());

       String pass = usuario.getContraseña();
txtNuevaPassword.setText(pass);
txtConfirmPassword.setText(pass);

    }

    @FXML
    private void onGuardarCambios() {
        if (usuario == null) {
            new Alert(Alert.AlertType.ERROR, "No hay usuario para editar.").showAndWait();
            return;
        }

        if (!validarCampos()) return;

        usuario.setNombre(txtNombre.getText().trim());
        usuario.setApellido(txtApellido.getText().trim());
        usuario.setUsername(txtUsername.getText().trim());
        usuario.setCorreo(txtCorreo.getText().trim());
        usuario.setContraseña(txtNuevaPassword.getText().trim());

        Respuesta resp = usuariosService.guardarUsuario(usuario);
        if (resp.getEstado()) {
            new Alert(Alert.AlertType.INFORMATION, "Usuario actualizado correctamente.").showAndWait();
            EditDeleteUserController ed =
              (EditDeleteUserController) FlowController.getInstance().getController("EditDeleteUser");
            ed.cargarUsuarios();
            getStage().close();
        } else {
            new Alert(Alert.AlertType.ERROR, "Error al actualizar: " + resp.getMensaje()).showAndWait();
        }
    }

    private boolean validarCampos() {
        if (txtNombre.getText().isBlank() ||
            txtApellido.getText().isBlank() ||
            txtUsername.getText().isBlank() ||
            txtCorreo.getText().isBlank() ||
            txtNuevaPassword.getText().isBlank() ||
            txtConfirmPassword.getText().isBlank()) {
            new Alert(Alert.AlertType.WARNING, "Todos los campos deben estar completos.").showAndWait();
            return false;
        }

        if (!txtNuevaPassword.getText().equals(txtConfirmPassword.getText())) {
            new Alert(Alert.AlertType.WARNING, "Las contraseñas no coinciden.").showAndWait();
            return false;
        }

        return true;
    }

    @FXML
    private void onVolver() {
        getStage().close();
    }

    @FXML
    private void onSalir() {
        Platform.exit();
    }
}
