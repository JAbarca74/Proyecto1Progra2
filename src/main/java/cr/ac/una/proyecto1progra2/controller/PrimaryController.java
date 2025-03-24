/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author jeffersonabarcap
 */
public class PrimaryController implements Initializable {

    @FXML
    private Button btnInicioSecion;
    @FXML
    private TextField txtUsuario;
    @FXML
    private TextField txtContraseña;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
private void switchToSecondary(ActionEvent event) throws IOException {
    String usuario = txtUsuario.getText();  
    String contraseña = txtContraseña.getText();  

    if (usuario.equals("admin") && contraseña.equals("admin")) {
        App.setRoot("optionsAdmin");
    } else {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error de inicio de sesión");
        alert.setHeaderText(null);
        alert.setContentText("Usuario o contraseña incorrectos.");
        alert.showAndWait();
    }
}

    
}
