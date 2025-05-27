package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.util.FlowController;
import cr.ac.una.proyecto1progra2.util.UserManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class PrincipalViewController extends Controller implements Initializable {

    @FXML
    private Button btnEditarUsuario;

    @FXML
    private Button btnRegisterNewAccount;

    @FXML
    private Button btnRegisterAdminAccount;

    @FXML
    private Button btnSalir;

    @FXML
    private Button btnReservar;
    
    @FXML
    private VBox VBoxMenuAdmin;

    @FXML
    private VBox VBoxMenuUsuario;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (UserManager.isAdmin()) {
            VBoxMenuAdmin.setVisible(true);          // Admin NO reserva
            VBoxMenuUsuario.setVisible(false); 
        } else if (UserManager.isRegularUser()) {
            VBoxMenuUsuario.setVisible(true);           // Usuario SÍ reserva
               VBoxMenuAdmin.setVisible(false);
        }
    }

    @FXML
    private void onActionBtnEditarUsuario(ActionEvent event) {
        FlowController.getInstance().goView("EditDeleteUser");
    }

    @FXML
    private void onRegisterNewAccount(ActionEvent event) {
        FlowController.getInstance().goView("RegisterNewAccount");
    }

    @FXML
    private void onRegisterAdminAccount(ActionEvent event) {
        FlowController.getInstance().goView("RegisterNewAdmin");
    }

    @FXML
    private void onActionBtnReservar(ActionEvent event) {
        FlowController.getInstance().goView("NewReservation");
    }
    @FXML
    private void onActionBtnEditarPiso(ActionEvent event) {
        FlowController.getInstance().goView("EditFloorAdmin");
    }

    @FXML
    private void onActionBtnSalir(ActionEvent event) {
        FlowController.getInstance().salir();
    }

    @Override
    public void initialize() {
        
    }
}
