package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.model.RolesDto;
import cr.ac.una.proyecto1progra2.model.UsuariosDto;
import cr.ac.una.proyecto1progra2.service.RolesService;
import cr.ac.una.proyecto1progra2.service.UsuariosService;
import cr.ac.una.proyecto1progra2.util.Respuesta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class UserManagerController extends Controller {

    @FXML private TableView<UsuariosDto> tblUsuarios;
    @FXML private TableColumn<UsuariosDto, Long>   colId;
    @FXML private TableColumn<UsuariosDto, String> colNombre;
    @FXML private TableColumn<UsuariosDto, String> colCorreo;
    @FXML private TableColumn<UsuariosDto, Long>   colRol;
    @FXML private TableColumn<UsuariosDto, String> colEstado;

    @FXML private TextField     txtNombre;
    @FXML private TextField     txtCorreo;
    @FXML private ChoiceBox<RolesDto> cbRol;
    @FXML private CheckBox      chkActivo;

    private final UsuariosService userService = new UsuariosService();
    private final RolesService   rolesService = new RolesService();
    private final ObservableList<UsuariosDto> lista = FXCollections.observableArrayList();

    @Override
    public void initialize() {
        // 1) Configuro columnas de la tabla
        colId.    setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colRol.   setCellValueFactory(new PropertyValueFactory<>("rolId"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        // 2) Cargo roles desde la base de datos
        Respuesta rr = rolesService.listarRoles();
        if (rr.getEstado()) {
            @SuppressWarnings("unchecked")
            var roles = (java.util.List<RolesDto>) rr.getResultado("Roles");
            cbRol.setItems(FXCollections.observableArrayList(roles));
        } else {
            mostrarError(rr.getMensaje());
        }

        // 3) Cargo la lista de usuarios
        cargarTabla();

        // 4) Listener para selección de fila
        tblUsuarios.getSelectionModel()
                   .selectedItemProperty()
                   .addListener((obs, oldU, newU) -> {
            if (newU != null) {
                txtNombre.setText(newU.getNombre());
                txtCorreo.setText(newU.getCorreo());
                // Selecciono el RolesDto cuyo ID coincide
                for (RolesDto r : cbRol.getItems()) {
                    if (r.getId().equals(newU.getRolId())) {
                        cbRol.setValue(r);
                        break;
                    }
                }
                chkActivo.setSelected("A".equals(newU.getEstado()));
            }
        });
    }

    private void cargarTabla() {
        lista.clear();
        Respuesta r = userService.listarUsuarios();
        if (r.getEstado()) {
            @SuppressWarnings("unchecked")
            var datos = (java.util.List<UsuariosDto>) r.getResultado("Usuarios");
            lista.addAll(datos);
        } else {
            mostrarError(r.getMensaje());
        }
        tblUsuarios.setItems(lista);
    }

    @FXML private void onNuevo() {
        tblUsuarios.getSelectionModel().clearSelection();
        txtNombre.clear();
        txtCorreo.clear();
        cbRol.getSelectionModel().clearSelection();
        chkActivo.setSelected(true);
    }

    @FXML private void onGuardar() {
        UsuariosDto dto = new UsuariosDto();
        dto.setNombre(txtNombre.getText());
        dto.setCorreo(txtCorreo.getText());
        dto.setRolId(cbRol.getValue().getId());
        dto.setEstado(chkActivo.isSelected() ? "A" : "I");

        var sel = tblUsuarios.getSelectionModel().getSelectedItem();
        if (sel != null) {
            dto.setId(sel.getId());
            dto.setVersion(sel.getVersion());
        }

        Respuesta r = userService.guardarUsuario(dto);
        if (!r.getEstado()) {
            mostrarError(r.getMensaje());
        }
        cargarTabla();
    }

    @FXML private void onEliminar() {
        var sel = tblUsuarios.getSelectionModel().getSelectedItem();
        if (sel == null) {
            mostrarError("Seleccione un usuario para eliminar.");
            return;
        }
        Respuesta r = userService.eliminarUsuario(sel.getId());
        if (!r.getEstado()) {
            mostrarError(r.getMensaje());
        }
        cargarTabla();
    }

    private void mostrarError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
