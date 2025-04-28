package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.util.FlowController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class UserManagerController extends Controller {

    @FXML
    private Button btnRegresar;
    @FXML
    private Button btnAdministradorUsuarios;
    @FXML
    private Button btnEditorEspacio;
    @FXML
    private Button btnEstadisticasGenerales;

    @Override
    public void initialize() {
        // Puedes hacer inicializaciones aquí si es necesario
    }

    @FXML
    private void onActionBtnRegresar(ActionEvent event) {
        FlowController.getInstance().goView("optionsAdmin");
    }

    @FXML
    private void onActionBtnAdministradorUsuarios(ActionEvent event) {
        FlowController.getInstance().goView("registerNewAccount");
    }

    @FXML
    private void onActionBtnEditorEspacio(ActionEvent event) {
        // Puedes programar aquí que abra otra pantalla, si existe
        // FlowController.getInstance().goView("editorEspacio");
    }

    @FXML
    private void onActionBtnEstadisticasGenerales(ActionEvent event) {
        // Puedes programar aquí que abra otra pantalla, si existe
        // FlowController.getInstance().goView("estadisticasGenerales");
    }
}
