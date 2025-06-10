package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.DTO.UsuariosDto;
import cr.ac.una.proyecto1progra2.service.UsuariosService;
import cr.ac.una.proyecto1progra2.util.FlowController;
import cr.ac.una.proyecto1progra2.util.Respuesta;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.control.TableCell;


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
    private TableColumn<UsuariosDto, String> colEstado;
    @FXML
    private TableColumn<UsuariosDto, String> colNombre;
    @FXML
    private TableColumn<UsuariosDto, String> colApellido;

    @FXML
    private TableColumn<UsuariosDto, String> colCorreo;

    @FXML
    private Button btnEditUser;
    @FXML
    private Button btnDeleteUser;
    @FXML
    private Button btnBuscar;

    private final UsuariosService usuariosService = new UsuariosService();
    private UsuariosDto usuarioSeleccionado;
    private List<UsuariosDto> usuariosOriginal = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    Respuesta resp = usuariosService.listarUsuarios();
    if (resp.getEstado()) {
        usuariosOriginal = (List<UsuariosDto>) resp.getResultado("Usuarios");
        tblUsuarios.setItems(FXCollections.observableArrayList(usuariosOriginal));
    } else {
        mostrarMensaje("Error al cargar usuarios: " + resp.getMensaje());
    }

    colId.setCellValueFactory(new PropertyValueFactory<>("id"));
    colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
    colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
    colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
    colRole.setCellValueFactory(new PropertyValueFactory<>("rolId"));

    colEstado.setCellFactory(column -> new TableCell<UsuariosDto, String>() {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                setText(null);
                setGraphic(null);
                return;
            }

            UsuariosDto usuario = getTableRow().getItem();
            boolean estadoActivo = usuario.getEstado(); 

            javafx.scene.shape.Rectangle rect = new javafx.scene.shape.Rectangle(10, 10);
            rect.setArcWidth(3);
            rect.setArcHeight(3);
            rect.setFill(estadoActivo ? javafx.scene.paint.Color.GREEN : javafx.scene.paint.Color.RED);

            setText(estadoActivo ? "A" : "I");
            setGraphic(rect);
            setGraphicTextGap(10);
        }
    });

    tblUsuarios.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
        usuarioSeleccionado = newVal;
        boolean habilitar = usuarioSeleccionado != null;
        btnEditUser.setDisable(!habilitar);
        btnDeleteUser.setDisable(!habilitar);
    });

    btnEditUser.setDisable(true);
    btnDeleteUser.setDisable(true);
}

    public void cargarUsuarios() {
    Respuesta resp = usuariosService.listarUsuarios();
    if (resp.getEstado()) {
        usuariosOriginal = (List<UsuariosDto>) resp.getResultado("Usuarios");
        tblUsuarios.setItems(FXCollections.observableArrayList(usuariosOriginal));
    } else {
        mostrarMensaje("Error al cargar usuarios: " + resp.getMensaje());
    }
}

    
   @Override
public void initialize() {
}


   @FXML
private void onActionBtnBuscar(javafx.event.Event event) {
    String filtro = txtBuscarUsuario.getText().trim().toLowerCase();
    if (filtro.isEmpty()) {
        tblUsuarios.setItems(FXCollections.observableArrayList(usuariosOriginal));
        btnEditUser.setDisable(true);
        btnDeleteUser.setDisable(true);
        return;
    }

    List<UsuariosDto> filtrados = usuariosOriginal.stream()
            .filter(u -> {
                String nombre = u.getNombre() != null ? u.getNombre().toLowerCase() : "";
                String username = u.getUsername() != null ? u.getUsername().toLowerCase() : "";
                return nombre.contains(filtro) || username.contains(filtro);
            })
            .collect(Collectors.toList());

    tblUsuarios.setItems(FXCollections.observableArrayList(filtrados));
    btnEditUser.setDisable(true);
    btnDeleteUser.setDisable(true);

    if (filtrados.isEmpty()) {
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
            usuariosOriginal.remove(usuarioSeleccionado);
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
        System.out.println(mensaje); 
    }

    @FXML
    private void onActionBtnEditar(ActionEvent event) {
        if (usuarioSeleccionado == null) {
            mostrarMensaje("Debe seleccionar un usuario para editar.");
            return;
        }

        EditUserController editUserController = (EditUserController) FlowController.getInstance().getController("EditUser");
        editUserController.setUsuario(usuarioSeleccionado);

        FlowController.getInstance().goViewInWindowModal("EditUser", getStage(), true);
    }
}
