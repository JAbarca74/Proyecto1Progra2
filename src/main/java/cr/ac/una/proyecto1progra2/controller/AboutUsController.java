package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.util.FlowController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class AboutUsController extends Controller implements Initializable {

    @FXML
    private void onActionBtnVolver(ActionEvent event) {
  FlowController.getInstance().goViewInWindow("Primary");

    }

    @FXML
    private void onActionBtnJefferson(ActionEvent event) {
        System.out.println("Jefferson clickeado");
    }

    @FXML
    private void onActionBtnHarold(ActionEvent event) {
        System.out.println("Harold clickeado");
    }

    @FXML
    private void onActionBtnCristhian(ActionEvent event) {
        System.out.println("Cristhian clickeado");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Si necesitás lógica de carga inicial, ponela aquí
    }

    @Override
    public void initialize() {
    }
}
