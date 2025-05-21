package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.model.UsuariosDto;
import cr.ac.una.proyecto1progra2.service.UsuariosService;
import cr.ac.una.proyecto1progra2.util.FlowController;
import cr.ac.una.proyecto1progra2.util.Respuesta;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class EditDeleteUserController extends Controller implements Initializable {

    @FXML
    private TextField txtBuscarUsuario;
    @FXML
    private TableView<UsuariosDto> tblUsuarios;
    @FXML
    private TableColumn<UsuariosDto, Long> colId;
    @FXML
    private TableColumn<UsuariosDto, String> colUsername;
    @FXML
    private TableColumn<UsuariosDto, String> colRole;
    @FXML
    private TableColumn<UsuariosDto, Boolean> colEstado;
    @FXML
    private Button btnEditUser;
    @FXML
    private Button btnDeleteUser;

    private final UsuariosService usuariosService = new UsuariosService();
    private UsuariosDto usuarioSeleccionado;
    @FXML
    private Button btnBuscar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
       colUsername.setCellValueFactory(new PropertyValueFactory<>("nombre"));
colRole.setCellValueFactory(new PropertyValueFactory<>("rolId"));
colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));


        tblUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            usuarioSeleccionado = newVal;
            boolean habilitar = usuarioSeleccionado != null;
            btnEditUser.setDisable(!habilitar);
            btnDeleteUser.setDisable(!habilitar);
        });

        btnEditUser.setDisable(true);
        btnDeleteUser.setDisable(true);
    }

    @FXML
    private void onActionBtnBuscar(ActionEvent event) {
        String nombre = txtBuscarUsuario.getText().trim();
        if (nombre.isEmpty()) {
            mostrarMensaje("Debe ingresar un nombre de usuario.");
            return;
        }

        Respuesta respuesta = usuariosService.listarUsuarios(); // usamos todos para filtrar por nombre
        if (!respuesta.getEstado()) {
            mostrarMensaje(respuesta.getMensaje());
            return;
        }

        List<UsuariosDto> todos = (List<UsuariosDto>) respuesta.getResultado("Usuarios");
        List<UsuariosDto> filtrados = new ArrayList<>();
        for (UsuariosDto u : todos) {
           if (u.getUsername() != null && u.getUsername().toLowerCase().contains(nombre.toLowerCase()))
 {
                filtrados.add(u);
            }
        }

        ObservableList<UsuariosDto> data = FXCollections.observableArrayList(filtrados);
        tblUsuarios.setItems(data);
        btnEditUser.setDisable(true);
        btnDeleteUser.setDisable(true);

        if (data.isEmpty()) {
            mostrarMensaje("No se encontraron usuarios.");
        }
    }
@FXML
    private void onActionBtnEliminar(ActionEvent event) {
        if (usuarioSeleccionado == null) {
            mostrarMensaje("Debe seleccionar un usuario para eliminar.");
            return;
        }

        Respuesta respuesta = usuariosService.eliminarUsuario(usuarioSeleccionado.getId());
        if (respuesta.getEstado()) {
            mostrarMensaje("Usuario eliminado correctamente.");
            tblUsuarios.getItems().remove(usuarioSeleccionado);
            tblUsuarios.getSelectionModel().clearSelection();
            usuarioSeleccionado = null;
            btnEditUser.setDisable(true);
            btnDeleteUser.setDisable(true);
        } else {
            mostrarMensaje(respuesta.getMensaje());
        }
    }

    private void mostrarMensaje(String mensaje) {
        System.out.println(mensaje); // Aquí puedes usar tu clase Utilities si la tienes
    }
    
@FXML
private void onActionBtnEditar(ActionEvent event) {
    if (usuarioSeleccionado == null) {
        mostrarMensaje("Debe seleccionar un usuario para editar.");
        return;
    }

    // Limpiar cualquier controlador anterior
    FlowController.getInstance().limpiarLoader("EditUser");

    // Obtener instancia del controlador y pasarle el usuario
    EditUserController editUserController = (EditUserController) FlowController.getInstance().getController("EditUser");
    editUserController.setUsuario(usuarioSeleccionado);

    // Abrir la nueva ventana
    FlowController.getInstance().goViewInWindow("EditUser");
    getStage().close();
}
    @FXML
    private void volver() {
        FlowController.getInstance().limpiarLoader("OptionsAdmin");
        FlowController.getInstance().goView("OptionsAdmin");
        
    }


    @Override
    public void initialize() {
        // llamado por FlowController si es necesario
    }
}
