
package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class UserManagerController implements Initializable {

    @FXML
    private Button btnRegresar;
    @FXML
    private Button btnAdministradorUsuarios;
    @FXML
    private Button btnEditorEspacio;
    @FXML
    private Button btnEstadisticasGenerales;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }  
    
      @FXML
private void switchToOptionsAdmin(ActionEvent event) throws IOException {
    App.setRoot("optionsAdmin");  
}
    
}
