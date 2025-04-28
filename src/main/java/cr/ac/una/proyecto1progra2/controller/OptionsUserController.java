package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.util.FlowController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class OptionsUserController extends Controller {

    @FXML
    private Button btnRegresar;
    @FXML
    private Button btnBuscarEspacio;
    @FXML
    private Button btnReservarEspacio;

    @Override
    public void initialize() {
        // Aquí puedes inicializar lo que necesites cuando cargue esta vista
    }

    @FXML
    private void onActionBtnRegresar(ActionEvent event) {
        FlowController.getInstance().goView("primary");
    }

    @FXML
    private void onActionBtnBuscarEspacio(ActionEvent event) {
        FlowController.getInstance().goView("buscarEspacio");
    }

    @FXML
    private void onActionBtnReservarEspacio(ActionEvent event) {
        FlowController.getInstance().goView("reservarEspacio");
    }
}
