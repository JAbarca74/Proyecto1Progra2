/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.util.FlowController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author harol
 */
public class PrincipalViewController extends Controller implements Initializable {

    @FXML
    private Button btnEditarUsuario;
    @FXML
    private Button btnSalir;

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
    private void onAcrionBtnEditarUsuario(ActionEvent event) {
        FlowController.getInstance().goView("EditDeleteUser");
    }

    @FXML
    private void onActionBtnSalir(ActionEvent event) {
        FlowController.getInstance().salir();
    }
}
