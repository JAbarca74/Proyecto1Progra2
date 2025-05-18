package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.model.UsuariosDto;
import cr.ac.una.proyecto1progra2.service.UsuariosService;
import cr.ac.una.proyecto1progra2.util.FlowController;
import cr.ac.una.proyecto1progra2.util.Respuesta;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class EditDeleteUserController extends Controller implements Initializable {

    @FXML
    private TextField txtBuscarUsuario;
    @FXML
    private VBox infoUsuarioBox;
    @FXML
    private Label lblId;
    @FXML
    private Label lblUsername;
    @FXML
    private Label lblRole;
    @FXML
    private Label lblEstado;
    @FXML
    private Button btnEditar;
    @FXML
    private Button btnEliminar;

    private final UsuariosService usuariosService = new UsuariosService();
    private UsuariosDto usuarioActual;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        infoUsuarioBox.setVisible(false);
    }

    @FXML
    private void onActionBtnBuscar(ActionEvent event) {
        String username = txtBuscarUsuario.getText().trim();
        if (username.isEmpty()) {
            mostrarMensaje("Debe ingresar un nombre de usuario.");
            return;
        }

        Respuesta respuesta = usuariosService.getUsuario(username, ""); // contraseña vacía porque solo busca por username

        if (respuesta.getEstado()) {
            usuarioActual = (UsuariosDto) respuesta.getResultado("Usuario");
            lblId.setText("ID: " + usuarioActual.getId());
            lblUsername.setText("Usuario: " + usuarioActual.getNombre());
            lblRole.setText("Rol: " + usuarioActual.getRolId());
            lblEstado.setText("Estado: " + (usuarioActual.getIsActive() ? "Activo" : "Inactivo"));
            infoUsuarioBox.setVisible(true);
        } else {
            mostrarMensaje(respuesta.getMensaje());
            infoUsuarioBox.setVisible(false);
        }
    }

    @FXML
    private void onActionBtnEditar(ActionEvent event) {
        if (usuarioActual == null) {
            mostrarMensaje("No hay usuario seleccionado.");
            return;
        }

        // Aquí podrías guardar el usuario actual en un controlador compartido si lo necesitas
        FlowController.getInstance().goViewInWindowModal("UserEditForm", getStage(), true);
    }

    @FXML
    private void onActionBtnEliminar(ActionEvent event) {
        if (usuarioActual == null) {
            mostrarMensaje("No hay usuario seleccionado.");
            return;
        }

        Respuesta respuesta = usuariosService.eliminarUsuario(usuarioActual.getId());
        if (respuesta.getEstado()) {
            mostrarMensaje("Usuario eliminado correctamente.");
            usuarioActual = null;
            infoUsuarioBox.setVisible(false);
        } else {
            mostrarMensaje(respuesta.getMensaje());
        }
    }

    private void mostrarMensaje(String mensaje) {
        // Puedes usar Utilities o Alert como prefieras
        System.out.println(mensaje); // Temporal: puedes cambiar esto por tu clase Utilities
    }

    @Override
    public void initialize() {
        // Esto es llamado por FlowController
    }
}
