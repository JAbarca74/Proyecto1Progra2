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

    @FXML private Label LabelPiso;
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

    private static final double CELL_WIDTH = 60;
    private static final double CELL_HEIGHT = 40;

    private Map<Integer, Integer> escritoriosPorPiso = new HashMap<>();
    private Map<Integer, Integer> salasPorPiso = new HashMap<>();
    private Map<Integer, Integer> areasPorPiso = new HashMap<>();
    private Map<Integer, Integer> libresPorPiso = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        spacesService = new SpacesService();
        configurarGrid();

        ComboBoxPiso.getItems().addAll("Piso 0", "Piso 1", "Piso 2", "Piso 3");
        ComboBoxPiso.getSelectionModel().selectFirst();

        LabelPiso.setText("Piso 0");
        cargarContadoresPorPiso(0);

        ComboBoxPiso.setOnAction(e -> {
            pisoActual = ComboBoxPiso.getSelectionModel().getSelectedIndex();
            LabelPiso.setText("Piso " + pisoActual);
            cargarContadoresPorPiso(pisoActual);
            cargarMatrizConEspacios();
        });

        cargarMatrizConEspacios();
    }

    private void cargarContadoresPorPiso(int piso) {
        LabelCanEscritorios.setText(String.valueOf(escritoriosPorPiso.getOrDefault(piso, 0)));
        LabelCanSalasComunes.setText(String.valueOf(salasPorPiso.getOrDefault(piso, 0)));
        LabelCantAreasComunes.setText(String.valueOf(areasPorPiso.getOrDefault(piso, 0)));
        LabelCantEspaciosLibres.setText(String.valueOf(libresPorPiso.getOrDefault(piso, 0)));
    }

    private void llenarCeldasLibres() {
        int numCols = 4;
        int numRows = 4;

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                boolean ocupado = false;
                for (SpaceVisual esp : espaciosAgregados) {
                    int r = esp.getRow();
                    int c = esp.getColumn();
                    int rs = esp.getRowSpan();
                    int cs = esp.getColSpan();
                    if (row >= r && row < r + rs && col >= c && col < c + cs) {
                        ocupado = true;
                        break;
                    }
                }
                if (!ocupado) {
                    StackPane celdaLibre = new StackPane();
                    celdaLibre.setPrefSize(CELL_WIDTH, CELL_HEIGHT);
                    celdaLibre.setStyle("-fx-border-color: gray; -fx-border-width: 1; -fx-background-color: white;");
                    gridMatrix.add(celdaLibre, col, row);
                }
            }
        }
    }

    private void configurarGrid() {
        gridMatrix.getColumnConstraints().clear();
        gridMatrix.getRowConstraints().clear();

        int numCols = 4;
        int numRows = 4;

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
        gridMatrix.getChildren().clear();

        escritoriosPorPiso.put(pisoActual, 0);
        salasPorPiso.put(pisoActual, 0);
        areasPorPiso.put(pisoActual, 0);
        libresPorPiso.put(pisoActual, 0);

        List<SpaceVisual> espacios = spacesService.obtenerEspaciosConPosicion()
                .stream()
                .filter(e -> e.getSpace().getNombre().contains("P" + pisoActual))
                .collect(Collectors.toList());

        for (SpaceVisual espacio : espacios) {
            espaciosAgregados.add(espacio);
            StackPane celda = crearCeldaEspacio(espacio);
            gridMatrix.add(celda, espacio.getColumn(), espacio.getRow(), espacio.getColSpan(), espacio.getRowSpan());

            String nombre = espacio.getSpace().getNombre().toLowerCase(Locale.ROOT);
            if (nombre.contains("e")) {
                escritoriosPorPiso.put(pisoActual, escritoriosPorPiso.get(pisoActual) + 1);
            } else if (nombre.contains("sala")) {
                salasPorPiso.put(pisoActual, salasPorPiso.get(pisoActual) + 1);
            } else if (nombre.contains("área")) {
                areasPorPiso.put(pisoActual, areasPorPiso.get(pisoActual) + 1);
            } else {
                libresPorPiso.put(pisoActual, libresPorPiso.get(pisoActual) + 1);
            }
        }
        cargarContadoresPorPiso(pisoActual);
        llenarCeldasLibres();
    }

    public StackPane crearCeldaEspacio(SpaceVisual espacio) {
        StackPane stack = new StackPane();
        Rectangle rect = new Rectangle(CELL_WIDTH * espacio.getColSpan(), CELL_HEIGHT * espacio.getRowSpan());
        rect.setArcWidth(10);
        rect.setArcHeight(10);

        String nombre = espacio.getSpace().getNombre().toLowerCase(Locale.ROOT);

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

    private SpaceVisual crearEspacioLibreConSpan(String nombre, int rowSpan, int colSpan) {
        int maxRows = 4;
        int maxCols = 4;

        for (int row = 0; row <= maxRows - rowSpan; row++) {
            for (int col = 0; col <= maxCols - colSpan; col++) {
                boolean ocupado = false;
                for (SpaceVisual esp : espaciosAgregados) {
                    int r = esp.getRow();
                    int c = esp.getColumn();
                    int rs = esp.getRowSpan();
                    int cs = esp.getColSpan();

                    if (row < r + rs && row + rowSpan > r && col < c + cs && col + colSpan > c) {
                        ocupado = true;
                        break;
                    }
                }
                if (!ocupado) {
                    SpacesDto nuevo = new SpacesDto();
                    nuevo.setNombre(nombre + " P" + pisoActual);
                    nuevo.setRow(row);
                    nuevo.setColumn(col);
                    nuevo.setRowSpan(rowSpan);
                    nuevo.setColSpan(colSpan);

                    spacesService.guardarSpace(nuevo);
                    return new SpaceVisual(nuevo, row, col, rowSpan, colSpan);
                }
            }
        }
        return null;
    }

    @FXML
    private void onAgregarEscritorios() {
    SpaceVisual nuevo = crearEspacioLibreConSpan("E" + (escritoriosPorPiso.getOrDefault(pisoActual, 0) + 1), 1, 1);
    if (nuevo != null) {
        int current = escritoriosPorPiso.getOrDefault(pisoActual, 0) + 1;
        escritoriosPorPiso.put(pisoActual, current);
        LabelCanEscritorios.setText(String.valueOf(current));

        espaciosAgregados.add(nuevo);
        StackPane celda = crearCeldaEspacio(nuevo);
        gridMatrix.add(celda, nuevo.getColumn(), nuevo.getRow(), nuevo.getColSpan(), nuevo.getRowSpan());
    } else {
        System.out.println("No hay espacio para más escritorios.");
    }
}

    @FXML
    private void onAgregarSalas() {
    int current = salasPorPiso.getOrDefault(pisoActual, 0);
    SpaceVisual nuevo = crearEspacioLibreConSpan("Sala " + (current + 1), 2, 2);
    if (nuevo != null) {
        salasPorPiso.put(pisoActual, current + 1);
        LabelCanSalasComunes.setText(String.valueOf(current + 1));

        espaciosAgregados.add(nuevo);
        StackPane celda = crearCeldaEspacio(nuevo);
        gridMatrix.add(celda, nuevo.getColumn(), nuevo.getRow(), nuevo.getColSpan(), nuevo.getRowSpan());
    } else {
        System.out.println("No hay espacio para más salas.");
    }
}
   


    @FXML
private void onAgregarAreasComunes() {
    int current = areasPorPiso.getOrDefault(pisoActual, 0);
    SpaceVisual nuevo = crearEspacioLibreConSpan("Área " + (current + 1), 1, 1);
    if (nuevo != null) {
        areasPorPiso.put(pisoActual, current + 1);
        LabelCantAreasComunes.setText(String.valueOf(current + 1));

        espaciosAgregados.add(nuevo);
        StackPane celda = crearCeldaEspacio(nuevo);
        gridMatrix.add(celda, nuevo.getColumn(), nuevo.getRow(), nuevo.getColSpan(), nuevo.getRowSpan());
    } else {
        System.out.println("No hay espacio para más áreas comunes.");
    }
}

    @FXML
   private void onAgregarEspaciosLibres() {
    int current = libresPorPiso.getOrDefault(pisoActual, 0);
    SpaceVisual nuevo = crearEspacioLibreConSpan("Libre " + (current + 1), 1, 1);
    if (nuevo != null) {
        libresPorPiso.put(pisoActual, current + 1);
        LabelCantEspaciosLibres.setText(String.valueOf(current + 1));

        espaciosAgregados.add(nuevo);
        StackPane celda = crearCeldaEspacio(nuevo);
        gridMatrix.add(celda, nuevo.getColumn(), nuevo.getRow(), nuevo.getColSpan(), nuevo.getRowSpan());
    } else {
        System.out.println("No hay espacio para más espacios libres.");
    }
}

    @FXML
    private void onBorrarTodo() {
        Respuesta resp = spacesService.eliminarTodosSpaces();
        if (resp.isSuccess()) {
            escritoriosPorPiso.clear();
            salasPorPiso.clear();
            areasPorPiso.clear();
            libresPorPiso.clear();
            espaciosAgregados.clear();

            LabelCanEscritorios.setText("0");
            LabelCanSalasComunes.setText("0");
            LabelCantAreasComunes.setText("0");
            LabelCantEspaciosLibres.setText("0");
            TexFieldAgregarPrecio.clear();
            ComboBoxPiso.getSelectionModel().selectFirst();
            LabelPiso.setText("Piso 0");
            gridMatrix.getChildren().clear();
            cargarMatrizConEspacios();
        }
    }

    @Override
    public void initialize() {
      
    }
}
