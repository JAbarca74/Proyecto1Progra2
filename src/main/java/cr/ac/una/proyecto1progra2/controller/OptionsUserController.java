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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author jeffersonabarcap
 */
public class OptionsUserController implements Initializable {

    @FXML
    private Button btnRegresar;
    @FXML
    private Button btnBuscarEspacio;
    @FXML
    private Button btnReservarEspacio;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void switchToPrimary(ActionEvent event) throws IOException {
    App.setRoot("primary");  
    }
    @FXML
    private AnchorPane contentPane;

    @FXML
    private void handleBuscarEspacio() throws IOException {
    AnchorPane pane = FXMLLoader.load(getClass().getResource("/cr/ac/una/proyecto1progra2/view/buscarEspacio.fxml"));
    contentPane.getChildren().setAll(pane);
    }

    @FXML
    private void handleReservarEspacio() throws IOException {
    AnchorPane pane = FXMLLoader.load(getClass().getResource("/cr/ac/una/proyecto1progra2/view/reservarEspacio.fxml"));
    contentPane.getChildren().setAll(pane);
    }
}
