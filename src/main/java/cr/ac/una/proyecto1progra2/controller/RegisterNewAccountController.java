package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.DTO.UsuariosDto;
import cr.ac.una.proyecto1progra2.service.UsuariosService;
import cr.ac.una.proyecto1progra2.util.Respuesta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;


public class RegisterNewAccountController extends Controller implements Initializable {

    @FXML private TextField txtUsername;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtPassword;
    @FXML private PasswordField txtPasswordConfirm;
    @FXML private TextField txtName;
    @FXML private TextField txtLastName;
    @FXML private Button btnCrearCuenta;

    private final UsuariosService usuariosService = new UsuariosService();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnCrearCuenta.disableProperty().bind(
            txtUsername.textProperty().isEmpty()
                .or(txtEmail.textProperty().isEmpty())
                .or(txtPassword.textProperty().isEmpty())
                .or(txtPasswordConfirm.textProperty().isEmpty())
                .or(txtName.textProperty().isEmpty())
                .or(txtLastName.textProperty().isEmpty())
        );
    }

    @FXML
    private void onActionBtnCrearCuenta(ActionEvent event) {
        boolean invalido = false;
        limpiarEstilos();

        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText();
        String confirmPassword = txtPasswordConfirm.getText();
        String nombre = txtName.getText().trim();
        String apellido = txtLastName.getText().trim();

        invalido |= marcarInvalido(txtUsername, username.isEmpty());
        invalido |= marcarInvalido(txtName, nombre.isEmpty() || !nombre.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+"));
        invalido |= marcarInvalido(txtLastName, apellido.isEmpty() || !apellido.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+"));
        invalido |= marcarInvalido(txtEmail, email.isEmpty() || !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$"));
        invalido |= marcarInvalido(txtPassword, password.isEmpty() || password.length() <= 4);
        invalido |= marcarInvalido(txtPasswordConfirm, !password.equals(confirmPassword));

        if (invalido) {
            mostrarAlerta("Por favor corrige los campos resaltados en rojo.");
            return;
        }

        UsuariosDto dto = new UsuariosDto();
        dto.setUsername(username);
        dto.setCorreo(email);
        dto.setContraseña(password);
        dto.setNombre(nombre);
        dto.setApellido(apellido);
        dto.setRolId(2L);

        Respuesta resp = usuariosService.guardarUsuario(dto);
        if (!resp.getEstado()) {
            mostrarAlerta("Error al guardar: " + resp.getMensaje());
            return;
        }

        mostrarInfo("¡Usuario creado correctamente!");
        limpiarFormulario();
    }

    @FXML
    private void onActionBtnCancelar(ActionEvent event) {
        boolean hayDatos =
            !txtUsername.getText().trim().isEmpty() ||
            !txtEmail.getText().trim().isEmpty() ||
            !txtPassword.getText().isEmpty() ||
            !txtPasswordConfirm.getText().isEmpty() ||
            !txtName.getText().trim().isEmpty() ||
            !txtLastName.getText().trim().isEmpty();

        if (!hayDatos) {
            cerrarVentana(event);
        } else {
            Alert alerta = new Alert(AlertType.CONFIRMATION);
            alerta.setTitle("Confirmación");
            alerta.setHeaderText("¿Cancelar registro?");
            alerta.setContentText("Se perderán los datos ingresados. ¿Está seguro de cancelar?");
            ButtonType btnSi = new ButtonType("Sí", ButtonBar.ButtonData.YES);
            ButtonType btnNo = new ButtonType("No", ButtonBar.ButtonData.NO);
            alerta.getButtonTypes().setAll(btnSi, btnNo);

            alerta.showAndWait().ifPresent(respuesta -> {
                if (respuesta == btnSi) {
                    cerrarVentana(event);
                }
            });
        }
    }

    private void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void limpiarFormulario() {
        txtUsername.clear();
        txtEmail.clear();
        txtPassword.clear();
        txtPasswordConfirm.clear();
        txtName.clear();
        txtLastName.clear();
        limpiarEstilos();
    }

    private void limpiarEstilos() {
        txtUsername.getStyleClass().remove("invalid");
        txtEmail.getStyleClass().remove("invalid");
        txtPassword.getStyleClass().remove("invalid");
        txtPasswordConfirm.getStyleClass().remove("invalid");
        txtName.getStyleClass().remove("invalid");
        txtLastName.getStyleClass().remove("invalid");
    }

    private boolean marcarInvalido(Control campo, boolean condicion) {
        if (condicion) {
            if (!campo.getStyleClass().contains("invalid")) {
                campo.getStyleClass().add("invalid");
            }
            return true;
        } else {
            campo.getStyleClass().remove("invalid");
            return false;
        }
    }

    private void mostrarAlerta(String mensaje) {
        new Alert(AlertType.WARNING, mensaje).showAndWait();
    }

    private void mostrarInfo(String mensaje) {
        new Alert(AlertType.INFORMATION, mensaje).showAndWait();
    }

    @Override
    public void initialize() {
    }
}
