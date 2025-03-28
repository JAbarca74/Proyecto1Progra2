package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.App;
import cr.ac.una.proyecto1progra2.service.UserService;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class PrimaryController implements Initializable {

    @FXML
    private Button btnInicioSecion;
    @FXML
    private TextField txtUsuario;
    @FXML
    private TextField txtContraseña;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }    

    @FXML
    private void switchToOptions(ActionEvent event) throws IOException {
        String usuario = txtUsuario.getText();
        String contraseña = txtContraseña.getText();

        if (UserService.validateAdimin(usuario, contraseña)) {  
           
            App.setRoot("optionsAdmin");
        }else if (UserService.validateUser(usuario, contraseña)) {  
           
            App.setRoot("optionsUser");
        }else {
            UserService.showAlert("Error de inicio de sesión", "Usuario o contraseña incorrectos.");
            
        }
    }
    
}
