package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.controller.MiniGameController;
import cr.ac.una.proyecto1progra2.util.FlowController;
import cr.ac.una.proyecto1progra2.util.UserManager;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PrincipalViewController extends Controller implements Initializable {

    @FXML private Button btnEditarUsuario;
    @FXML private Button btnRegisterNewAccount;
    @FXML private Button btnRegisterAdminAccount;
    @FXML private Button btnSalir;
    @FXML private Button btnGestionReservas;
    @FXML private Button btnReservar;
    @FXML private Button btnStatistics;
    @FXML private Button btnSalir1;
    @FXML private Button btnMiniJuego;

    @FXML private VBox VBoxMenuAdmin;
    @FXML private VBox VBoxMenuUsuario;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (UserManager.isAdmin()) {
            VBoxMenuAdmin.setVisible(true);
            VBoxMenuUsuario.setVisible(false);
            Platform.runLater(() -> {
                FlowController.getInstance().limpiarLoader("WelcomeView");
                FlowController.getInstance().goView("WelcomeView");
            });
        } else if (UserManager.isRegularUser()) {
            VBoxMenuUsuario.setVisible(true);
            VBoxMenuAdmin.setVisible(false);
        }
    }

    @FXML
    private void onActionBtnEditarUsuario(ActionEvent event) {
        FlowController.getInstance().limpiarLoader("EditDeleteUser");
        FlowController.getInstance().goView("EditDeleteUser");
    }

    @FXML
    private void onRegisterAdminAccount(ActionEvent event) {
        FlowController.getInstance().limpiarLoader("RegisterNewAdmin");
        FlowController.getInstance().goView("RegisterNewAdmin");
    }

    @FXML
    private void onActionBtnReservar(ActionEvent event) {
        FlowController.getInstance().limpiarLoader("NewReservation");
        FlowController.getInstance().goView("NewReservation");
    }

    @FXML
    private void onActionBtnEditarPiso(ActionEvent event) {
        FlowController.getInstance().limpiarLoader("EditFloorAdmin");
        FlowController.getInstance().goView("EditFloorAdmin");
    }

    @FXML
    private void onActionBtnSalir(ActionEvent event) {
        Stage ventanaActual = (Stage) btnSalir.getScene().getWindow();
        ventanaActual.close();
        FlowController.getInstance().limpiarLoader("Primary");
        FlowController.getInstance().goViewInWindow("Primary");
    }

    @FXML
    private void onActionBtnSalir1(ActionEvent event) {
        Stage ventanaActual = (Stage) btnSalir1.getScene().getWindow();
        ventanaActual.close();
        FlowController.getInstance().limpiarLoader("Primary");
        FlowController.getInstance().goViewInWindow("Primary");
    }

    @FXML
    private void onStatistics(ActionEvent event) {
        FlowController.getInstance().limpiarLoader("Reports");
        FlowController.getInstance().goView("Reports");
    }

    @FXML
    private void onGestionReservas(ActionEvent event) {
        FlowController.getInstance().limpiarLoader("ReservationManagement");
        FlowController.getInstance().goView("ReservationManagement");
    }

    @FXML
    private void onActionMiniJuego(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/cr/ac/una/proyecto1progra2/view/MiniGame.fxml")
            );
            Parent miniRoot = loader.load();

            Stage miniStage = new Stage();
            Stage owner = (Stage) ((Node) event.getSource()).getScene().getWindow();
            miniStage.initOwner(owner);
            miniStage.initModality(Modality.APPLICATION_MODAL);
            miniStage.setTitle("Mini Juego");
            miniStage.setScene(new Scene(miniRoot));

            MiniGameController ctrl = loader.getController();
            ctrl.setStage(miniStage);

            miniStage.showAndWait();

        } catch (IOException ex) {
            ex.printStackTrace();
            mostrarAlerta("No se pudo cargar el mini-juego:\n" + ex.getMessage());
        }
    }

    @Override
    public void initialize() {
        // No usado
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
