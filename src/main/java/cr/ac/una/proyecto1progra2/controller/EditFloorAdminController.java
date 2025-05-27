package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.DTO.SpacesDto;
import cr.ac.una.proyecto1progra2.service.SpacesService;
import cr.ac.una.proyecto1progra2.util.Respuesta;
import cr.ac.una.proyecto1progra2.util.SpaceVisual;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;
import java.util.ResourceBundle;

public class EditFloorAdminController extends Controller implements Initializable {

    @FXML private Label LabelCanEscritorios;
    @FXML private Label LabelCanSalasComunes;
    @FXML private Label LabelCantAreasComunes;
    @FXML private Label LabelCantEspaciosLibres;
    @FXML private ComboBox<String> ComboBoxPiso;
    @FXML private TextField TexFieldAgregarPrecio;
    @FXML private GridPane gridMatrix;

    private SpacesService spacesService;
    private List<SpaceVisual> espaciosAgregados = new ArrayList<>();
    private int pisoActual = 0;

    // Tamaño fijo para celdas
    private static final double CELL_WIDTH = 60;
    private static final double CELL_HEIGHT = 40;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        spacesService = new SpacesService();

        configurarGrid();

        ComboBoxPiso.getItems().addAll("Piso 0", "Piso 1", "Piso 2", "Piso 3");
        ComboBoxPiso.getSelectionModel().selectFirst();

        ComboBoxPiso.setOnAction(e -> {
            pisoActual = ComboBoxPiso.getSelectionModel().getSelectedIndex();
            cargarMatrizConEspacios();
        });

        LabelCanEscritorios.setText("0");
        LabelCanSalasComunes.setText("0");
        LabelCantAreasComunes.setText("0");
        LabelCantEspaciosLibres.setText("0");

        cargarMatrizConEspacios();
    }

    // Configura columnas y filas con tamaño fijo para evitar superposición
    private void configurarGrid() {
        gridMatrix.getColumnConstraints().clear();
        gridMatrix.getRowConstraints().clear();

        int numCols = 9;
        int numRows = 8;

        for (int i = 0; i < numCols; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPrefWidth(CELL_WIDTH);
            gridMatrix.getColumnConstraints().add(cc);
        }

        for (int i = 0; i < numRows; i++) {
            RowConstraints rc = new RowConstraints();
            rc.setPrefHeight(CELL_HEIGHT);
            gridMatrix.getRowConstraints().add(rc);
        }
    }

    public void cargarMatrizConEspacios() {
        espaciosAgregados.clear();
        List<SpaceVisual> espacios = spacesService.obtenerEspaciosConPosicion()
            .stream()
            .filter(e -> e.getSpace().getNombre().contains("P" + pisoActual))
            .collect(Collectors.toList());

        gridMatrix.getChildren().clear();

        for (SpaceVisual espacio : espacios) {
            espaciosAgregados.add(espacio);
            StackPane cell = crearCeldaEspacio(espacio);
            gridMatrix.add(cell, espacio.getColumn(), espacio.getRow(), espacio.getColSpan(), espacio.getRowSpan());
        }
    }

    public StackPane crearCeldaEspacio(SpaceVisual espacio) {
        StackPane stack = new StackPane();
        Rectangle rect = new Rectangle(CELL_WIDTH * espacio.getColSpan(), CELL_HEIGHT * espacio.getRowSpan());
        rect.setArcWidth(10);
        rect.setArcHeight(10);

        String nombre = espacio.getSpace().getNombre().toLowerCase();

        if (nombre.contains("sala")) {
            rect.setFill(Color.CRIMSON);
        } else if (nombre.contains("área")) {
            rect.setFill(Color.DARKGREEN);
        } else if (nombre.contains("e")) {
            rect.setFill(Color.DODGERBLUE);
        } else {
            rect.setFill(Color.LIGHTGRAY);
        }

        rect.setStroke(Color.BLACK);

        Text text = new Text(espacio.getSpace().getNombre());
        text.setFill(Color.WHITE);

        stack.getChildren().addAll(rect, text);
        return stack;
    }

    private SpaceVisual crearEspacioLibre(String nombre) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                boolean ocupado = false;
                for (SpaceVisual esp : espaciosAgregados) {
                    int r = esp.getRow(), c = esp.getColumn();
                    int rs = esp.getRowSpan(), cs = esp.getColSpan();
                    if (row >= r && row < r + rs && col >= c && col < c + cs) {
                        ocupado = true;
                        break;
                    }
                }
                if (!ocupado) {
                    SpacesDto nuevo = new SpacesDto();
                    nuevo.setNombre(nombre + " P" + pisoActual);
                    spacesService.guardarSpace(nuevo);
                    return new SpaceVisual(nuevo, row, col, 1, 1);
                }
            }
        }
        return null;
    }

    @FXML private void onAgregarEscritorios() {
        int current = Integer.parseInt(LabelCanEscritorios.getText());
        LabelCanEscritorios.setText(String.valueOf(current + 1));
        SpaceVisual nuevo = crearEspacioLibre("E" + (current + 1));
        if (nuevo != null) {
            espaciosAgregados.add(nuevo);
            StackPane celda = crearCeldaEspacio(nuevo);
            gridMatrix.add(celda, nuevo.getColumn(), nuevo.getRow(), nuevo.getColSpan(), nuevo.getRowSpan());
        }
    }

    @FXML private void onAgregarSalas() {
        int current = Integer.parseInt(LabelCanSalasComunes.getText());
        LabelCanSalasComunes.setText(String.valueOf(current + 1));
        SpaceVisual nuevo = crearEspacioLibre("Sala " + (current + 1));
        if (nuevo != null) {
            espaciosAgregados.add(nuevo);
            StackPane celda = crearCeldaEspacio(nuevo);
            gridMatrix.add(celda, nuevo.getColumn(), nuevo.getRow(), nuevo.getColSpan(), nuevo.getRowSpan());
        }
    }

    @FXML private void onAgregarAreasComunes() {
        int current = Integer.parseInt(LabelCantAreasComunes.getText());
        LabelCantAreasComunes.setText(String.valueOf(current + 1));
        SpaceVisual nuevo = crearEspacioLibre("Área " + (current + 1));
        if (nuevo != null) {
            espaciosAgregados.add(nuevo);
            StackPane celda = crearCeldaEspacio(nuevo);
            gridMatrix.add(celda, nuevo.getColumn(), nuevo.getRow(), nuevo.getColSpan(), nuevo.getRowSpan());
        }
    }

    @FXML private void onAgregarEspaciosLibres() {
        int current = Integer.parseInt(LabelCantEspaciosLibres.getText());
        LabelCantEspaciosLibres.setText(String.valueOf(current + 1));
        SpaceVisual nuevo = crearEspacioLibre("Libre " + (current + 1));
        if (nuevo != null) {
            espaciosAgregados.add(nuevo);
            StackPane celda = crearCeldaEspacio(nuevo);
            gridMatrix.add(celda, nuevo.getColumn(), nuevo.getRow(), nuevo.getColSpan(), nuevo.getRowSpan());
        }
    }

    @FXML
    private void onBorrarTodo() {
        Respuesta resp = spacesService.eliminarTodosSpaces();
        if (resp.isSuccess()) {
            espaciosAgregados.clear();
            LabelCanEscritorios.setText("0");
            LabelCanSalasComunes.setText("0");
            LabelCantAreasComunes.setText("0");
            LabelCantEspaciosLibres.setText("0");
            TexFieldAgregarPrecio.clear();
            ComboBoxPiso.getSelectionModel().selectFirst();
            gridMatrix.getChildren().clear();
            cargarMatrizConEspacios();
        } else {
            System.out.println("Error borrando espacios: " + resp.getMensaje());
        }
    }

    @Override
    public void initialize() {
        // No hacer nada aquí, ya está el initialize con parámetros
    }
}