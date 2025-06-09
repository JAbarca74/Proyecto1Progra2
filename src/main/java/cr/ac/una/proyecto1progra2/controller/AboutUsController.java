package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.util.FlowController;
import cr.ac.una.proyecto1progra2.util.MusicManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;

public class AboutUsController extends Controller implements Initializable {

   @FXML
private void onActionBtnVolver(ActionEvent event) {
    // Cierra la ventana actual
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.close();
}


  @FXML private void onActionBtnJefferson() {
    MusicManager.playEffect("jefferson.mp3", false);
}
@FXML private void onActionBtnHarold() {
    MusicManager.playEffect("harold.mp3", false);
}
@FXML private void onActionBtnCristhian() {
    MusicManager.playEffect("cristhian.mp3", false);
}


    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @Override
    public void initialize() {
    }
}
