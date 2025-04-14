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

/**
 * FXML Controller class
 *
 * @author jeffersonabarcap
 */
public class UserManagerController implements Initializable {

    @FXML
    private Button btnRegresar;
    @FXML
    private Button btnAdministradorUsuarios;
    @FXML
    private Button btnEditorEspacio;
    @FXML
    private Button btnEstadisticasGenerales;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    
     @FXML
    private void switchToOptionsAdmin(ActionEvent event) throws IOException {
    App.setRoot("optionsAdmin");  
    }

     @FXML
    private void switchToRegisterNewAccount(ActionEvent event) throws IOException {
    App.setRoot("RegisterNewAccount");  
    }

}
