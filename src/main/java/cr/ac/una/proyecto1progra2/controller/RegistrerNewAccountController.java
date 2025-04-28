package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.util.FlowController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegistrerNewAccountController extends Controller {

    @FXML
    private Button btnRegresar;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtId;
    @FXML
    private TextField txtAge;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private PasswordField txtPasswordConfirm;
    @FXML
    private CheckBox chkAdmin;
    @FXML
    private CheckBox chkUser;
    @FXML
    private Button btnCrearCuenta;

    @Override
    public void initialize() {
        // Aquí podrías limpiar campos, setear defaults, etc.
    }

    @FXML
    private void onActionBtnRegresar(ActionEvent event) {
        FlowController.getInstance().goView("userManager");
    }
}
