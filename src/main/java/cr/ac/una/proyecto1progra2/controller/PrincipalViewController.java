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
import javafx.stage.Stage;

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
    private Button btnGestionReservas;

    @FXML
    private Button btnReservar;
    
    @FXML
    private Button btnStatistics;
    
    @FXML
    private VBox VBoxMenuAdmin;

    @FXML
    private VBox VBoxMenuUsuario;
    
    @FXML
    private Button btnSalir1;
    
    @Override
public void initialize(URL url, ResourceBundle rb) {
    if (UserManager.isAdmin()) {
        VBoxMenuAdmin.setVisible(true);
        VBoxMenuUsuario.setVisible(false);

        // Evita el error usando Platform.runLater
        javafx.application.Platform.runLater(() -> {
            FlowController.getInstance().goView("WelcomeView");
        });

    } else if (UserManager.isRegularUser()) {
        VBoxMenuUsuario.setVisible(true);
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
        // Cierra la ventana actual
    Stage ventanaActual = (Stage) btnSalir.getScene().getWindow();
    ventanaActual.close();

    // Limpia el loader si deseas reiniciar su estado
    FlowController.getInstance().limpiarLoader("Primary");

    // Abre la ventana de login
    FlowController.getInstance().goViewInWindow("Primary");
    }
    
    @FXML
    private void onActionBtnSalir1(ActionEvent event) {
        // Cierra la ventana actual
    Stage ventanaActual = (Stage) btnSalir1.getScene().getWindow();
    ventanaActual.close();

    // Limpia el loader si deseas reiniciar su estado
    FlowController.getInstance().limpiarLoader("Primary");

    // Abre la ventana de login
    FlowController.getInstance().goViewInWindow("Primary");
    }
    
     @FXML
    private void onStatistics(ActionEvent event) {
        FlowController.getInstance().goView("Reports");
    }
    
    @FXML
    private void onGestionReservas(ActionEvent event) {
        // Carga ReservationManagement.fxml en el CENTER del BorderPane principal
        FlowController.getInstance().goView("ReservationManagement");
    }
    
    @Override
    public void initialize() {
        
    }
}
