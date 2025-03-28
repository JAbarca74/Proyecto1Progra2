package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class OptionsAdminController implements Initializable {

    @FXML
    private Button btnRegresar;
    @FXML
    private Button btnEstadisticas;
    @FXML
    private Button btnRegistro;
    @FXML
    private Button btnEditor;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
    }    

  
    @FXML
private void switchToPrimary(ActionEvent event) throws IOException {
    App.setRoot("primary");  
}

    
}
