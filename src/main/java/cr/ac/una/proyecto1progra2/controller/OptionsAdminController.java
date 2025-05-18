package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.util.FlowController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class OptionsAdminController extends Controller {

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
        // Puedes poner inicialización de botones o datos aquí si quieres
    }

    @FXML
    private void onActionBtnRegresar(ActionEvent event) {
        FlowController.getInstance().goView("primary");
    }
   
    @FXML
    private void onActionBtnEditor(ActionEvent event) {
        // Aquí puedes navegar a otra pantalla si quieres
        // FlowController.getInstance().goView("editorView");
    }

    @FXML
    private void onActionBtnEstadisticas(ActionEvent event) {
        // Aquí puedes navegar a una pantalla de estadísticas si quieres
        // FlowController.getInstance().goView("estadisticasView");
    }
    @FXML
    private void onActionBtnRegistro(ActionEvent event) {
    FlowController.getInstance().goView("EditDeleteUser");
}
}
