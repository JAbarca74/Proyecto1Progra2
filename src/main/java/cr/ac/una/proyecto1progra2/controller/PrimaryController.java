package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.App;
import java.io.IOException;
import javafx.fxml.FXML;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
