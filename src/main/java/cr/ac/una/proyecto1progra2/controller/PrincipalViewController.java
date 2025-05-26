package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.model.UsuariosDto;
import cr.ac.una.proyecto1progra2.util.FlowController;
<<<<<<< Updated upstream
import java.io.IOException;
=======
import cr.ac.una.proyecto1progra2.util.UserManager;
>>>>>>> Stashed changes
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
<<<<<<< Updated upstream
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
=======
>>>>>>> Stashed changes

public class PrincipalViewController extends Controller implements Initializable {

<<<<<<< Updated upstream
    @FXML private Button btnEditarUsuario;
    @FXML private Button btnRegisterNewAccount;
    @FXML private Button btnRegisterAdminAccount;
    @FXML private Button btnSalir;
=======
    @FXML
    private Button btnEditarUsuario;

    @FXML
    private Button btnSalir;
>>>>>>> Stashed changes

    @FXML
    private Button btnReservar;

    @Override
  public void initialize(URL url, ResourceBundle rb) {

    if (UserManager.isAdmin()) {
        btnReservar.setVisible(false);          // Admin NO reserva
        btnEditarUsuario.setVisible(true);      // Admin SÍ edita usuarios
    } else if (UserManager.isRegularUser()) {
        btnReservar.setVisible(true);           // Usuario SÍ reserva
        btnEditarUsuario.setVisible(false);     // Usuario NO ve botón de editar
    }
}

   
     @FXML
    private void onActionBtnEditarUsuario(ActionEvent event) {
        FlowController.getInstance().goView("EditDeleteUser");
    }

    // Botón para ir a la vista de registro de nuevo usuario
    @FXML
    private void onRegisterNewAccount(ActionEvent event) {
        FlowController.getInstance().goView("RegisterNewAccount");
    }

    // Botón para ir a la vista de registro de nuevo administrador
    @FXML
    private void onRegisterAdminAccount(ActionEvent event) {
        FlowController.getInstance().goView("RegisterNewAdmin");
    }

    // Botón para salir de la aplicación
    @FXML
    private void onActionBtnReservar(ActionEvent event) {
        FlowController.getInstance().goView("NewReservation"); // Ajusta al nombre real de tu vista
    }

    @FXML
    private void onActionBtnSalir(ActionEvent event) {
        FlowController.getInstance().salir();
    }

    @Override
    public void initialize() {
       
    }
}