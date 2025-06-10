package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.util.MusicManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class AboutUsController extends Controller implements Initializable {

   @FXML
private void onActionBtnVolver(ActionEvent event) {
    MusicManager.stopEffect(); 
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.close();
}

@Override
public void setStage(Stage stage) {
    super.setStage(stage);                       
    if (stage != null) {
        stage.addEventHandler(WindowEvent.WINDOW_HIDDEN,
            e -> MusicManager.stopEffect());     
    }
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
