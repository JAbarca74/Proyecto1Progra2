package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.model.UsuariosDto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class EditUserController extends Controller {  // <- IMPORTANTE: extiende de Controller

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtPassword;
    @FXML
    private Button btnGuardar;

    private UsuariosDto usuario;

    public void setUsuario(UsuariosDto usuario) {
        this.usuario = usuario;
        txtNombre.setText(usuario.getNombre());
    
    }

    @FXML
    private void onGuardarCambios(ActionEvent event) {
        System.out.println("Guardando cambios del usuario: " + usuario.getId());
        // lógica para actualizar usuario
    }

    @Override
    public void initialize() {
        // si usás FlowController, se llama al iniciar la vista
    }
}
