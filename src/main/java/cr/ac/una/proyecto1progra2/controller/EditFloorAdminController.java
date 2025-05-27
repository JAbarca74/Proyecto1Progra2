package cr.ac.una.proyecto1progra2.controller;

import javafx.fxml.FXML;
import cr.ac.una.proyecto1progra2.util.FlowController;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class EditFloorAdminController extends Controller implements Initializable {

    @FXML private Label LabelPiso;
    @FXML private Label LabelCanEscritorios;
    @FXML private Label LabelCanSalasComunes;
    @FXML private Label LabelCantAreasComunes;
    @FXML private Label LabelCantEspaciosLibres;

    @FXML private Button BtnAgregarEscritorios;
    @FXML private Button BtnAgregarSalas;
    @FXML private Button BtnAgregarAreasComunes;
    @FXML private Button BtnAgregarEspaciosLibres;
    @FXML private Button BtnBorrarTodo;

    @FXML private ComboBox<String> ComboBoxPiso;
    @FXML private TextField TexFieldAgregarPrecio;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializar ComboBox con pisos disponibles
        ComboBoxPiso.getItems().addAll("Piso 0", "Piso 1", "Piso 2", "Piso 3");
        ComboBoxPiso.getSelectionModel().selectFirst();

        // Inicializar labels con valores predeterminados
        LabelCanEscritorios.setText("0");
        LabelCanSalasComunes.setText("0");
        LabelCantAreasComunes.setText("0");
        LabelCantEspaciosLibres.setText("0");
    }

    @FXML
    private void onAgregarEscritorios() {
        int current = Integer.parseInt(LabelCanEscritorios.getText());
        LabelCanEscritorios.setText(String.valueOf(current + 1));
        System.out.println("Escritorio agregado");
    }

    @FXML
    private void onAgregarSalas() {
        int current = Integer.parseInt(LabelCanSalasComunes.getText());
        LabelCanSalasComunes.setText(String.valueOf(current + 1));
        System.out.println("Sala agregada");
    }

    @FXML
    private void onAgregarAreasComunes() {
        int current = Integer.parseInt(LabelCantAreasComunes.getText());
        LabelCantAreasComunes.setText(String.valueOf(current + 1));
        System.out.println("Área común agregada");
    }

    @FXML
    private void onAgregarEspaciosLibres() {
        int current = Integer.parseInt(LabelCantEspaciosLibres.getText());
        LabelCantEspaciosLibres.setText(String.valueOf(current + 1));
        System.out.println("Espacio libre agregado");
    }

    @FXML
    private void onBorrarTodo() {
        LabelCanEscritorios.setText("0");
        LabelCanSalasComunes.setText("0");
        LabelCantAreasComunes.setText("0");
        LabelCantEspaciosLibres.setText("0");
        TexFieldAgregarPrecio.clear();
        ComboBoxPiso.getSelectionModel().selectFirst();
        System.out.println("Datos borrados");
    }

    // No override sin parámetros para initialize, para evitar excepciones

    @Override
    public void initialize() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}