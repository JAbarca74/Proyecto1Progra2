/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.util.FlowController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author harol
 */
public class PrincipalViewController extends Controller implements Initializable {

    @FXML private Button btnEditarUsuario;
    @FXML private Button btnRegisterNewAccount;
    @FXML private Button btnRegisterAdminAccount;
    @FXML private Button btnSalir;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @Override
    public void initialize() {
    }

   
     @FXML
    private void onActionBtnEditarUsuario(ActionEvent event) {
        FlowController.getInstance().goView("EditDeleteUser");
    }

    // Botón para ir a la vista de registro de nuevo usuario
    @FXML
    private void onRegisterNewAccount(ActionEvent event) {
        FlowController.getInstance().goView("RegisterNewAccount");
    }

    // Botón para ir a la vista de registro de nuevo administrador
    @FXML
    private void onRegisterAdminAccount(ActionEvent event) {
        FlowController.getInstance().goView("RegisterNewAdmin");
    }

    // Botón para salir de la aplicación
    @FXML
    private void onActionBtnSalir(ActionEvent event) {
        FlowController.getInstance().salir();
    }
}
