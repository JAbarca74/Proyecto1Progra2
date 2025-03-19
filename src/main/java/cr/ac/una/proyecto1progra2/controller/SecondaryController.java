package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.App;
import java.io.IOException;
import javafx.fxml.FXML;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}