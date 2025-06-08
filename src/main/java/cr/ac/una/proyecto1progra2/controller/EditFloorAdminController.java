package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.DTO.SpacesDto;
import cr.ac.una.proyecto1progra2.service.SpacesService;
import cr.ac.una.proyecto1progra2.util.Respuesta;
import cr.ac.una.proyecto1progra2.util.SpaceVisual;
import cr.ac.una.proyecto1progra2.util.Utilities;
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
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class EditFloorAdminController extends Controller implements Initializable {

    @FXML private Label LabelPiso;
    @FXML private Label LabelCanEscritorios;
    @FXML private Label LabelCanSalasComunes;
    @FXML private Label LabelCantAreasComunes;
    @FXML private Label LabelCantEspaciosLibres;
    @FXML private ComboBox<String> ComboBoxPiso;
    @FXML private TextField TexFieldAgregarPrecio;
    @FXML private GridPane gridMatrix;
    @FXML private Label LabelCapacidadTotal;@FXML private StackPane rootPane;
    private Rectangle celdaResaltada;
    
    private StackPane stackSeleccionado = null;
private SpaceVisual espacioSeleccionado = null;
private double offsetX;
private double offsetY;
    private SpacesService spacesService;
    private List<SpaceVisual> espaciosAgregados = new ArrayList<>();
    private int pisoActual = 0;

    private static final double CELL_WIDTH = 60;
    private static final double CELL_HEIGHT = 40;

    private Map<Integer, Integer> escritoriosPorPiso = new HashMap<>();
    private Map<Integer, Integer> salasPorPiso = new HashMap<>();
    private Map<Integer, Integer> areasPorPiso = new HashMap<>();
    private Map<Integer, Integer> libresPorPiso = new HashMap<>();
private final List<Rectangle> celdasResaltadas = new ArrayList<>();
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
public StackPane crearCeldaEspacio(SpaceVisual espacio) {
    StackPane stack = new StackPane();
    Rectangle rect = new Rectangle(CELL_WIDTH * espacio.getColSpan(), CELL_HEIGHT * espacio.getRowSpan());
    rect.setArcWidth(10);
    rect.setArcHeight(10);
    String nombre = espacio.getSpace().getNombre().toLowerCase(Locale.ROOT);
    if (nombre.contains("sala")) rect.setFill(Color.CRIMSON);
    else if (nombre.contains("área")) rect.setFill(Color.DARKGREEN);
    else if (nombre.contains("libre")) rect.setFill(Color.GOLD);
    else if (nombre.contains("e")) rect.setFill(Color.DODGERBLUE);
    else rect.setFill(Color.LIGHTGRAY);
    rect.setStroke(Color.BLACK);

    Text text = new Text(espacio.getSpace().getNombre());
    text.setFill(Color.WHITE);
    stack.getChildren().addAll(rect, text);

    // Al hacer clic se selecciona para mover
    stack.setOnMouseClicked(event -> {
        espacioSeleccionado = espacio;
        stackSeleccionado = stack;

        // Agregar borde visual al seleccionado
        stack.setStyle("-fx-border-color: yellow; -fx-border-width: 3; -fx-border-radius: 5;");
        Utilities.mostrarMensaje("Movimiento activado", "Haz clic en un espacio vacío para moverlo.");
        event.consume();
    });

    return stack;
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

                final int finalRow = row;
                final int finalCol = col;

                celdaLibre.setOnMouseClicked(event -> {
                    if (espacioSeleccionado != null && stackSeleccionado != null) {
                        int rowSpan = espacioSeleccionado.getRowSpan();
                        int colSpan = espacioSeleccionado.getColSpan();

                        boolean dentro = finalCol + colSpan <= 4 && finalRow + rowSpan <= 4;
                        if (!dentro) {
                            Utilities.mostrarMensaje("Posición inválida", "El espacio no cabe en esa posición.");
                            return;
                        }

                        // Verificar si ya hay otro espacio en esa zona
                        boolean ocupadoDestino = false;
                        for (SpaceVisual esp : espaciosAgregados) {
                            if (esp != espacioSeleccionado) {
                                int r = esp.getRow();
                                int c = esp.getColumn();
                                int rs = esp.getRowSpan();
                                int cs = esp.getColSpan();
                                if (finalRow < r + rs && finalRow + rowSpan > r &&
                                    finalCol < c + cs && finalCol + colSpan > c) {
                                    ocupadoDestino = true;
                                    break;
                                }
                            }
                        }

                        if (ocupadoDestino) {
                            Utilities.mostrarMensaje("Espacio ocupado", "Ya hay otro espacio en esa posición.");
                            return;
                        }

                        // Quitar visualmente el stack anterior
                        gridMatrix.getChildren().remove(stackSeleccionado);

                        // Actualizar modelo y DTO
                        espacioSeleccionado.setRow(finalRow);
                        espacioSeleccionado.setColumn(finalCol);
                        SpacesDto dto = espacioSeleccionado.getSpace();
                        dto.setRow(finalRow);
                        dto.setColumn(finalCol);

                        // Guardar en la base de datos
                        Respuesta r = spacesService.guardarSpace(dto);
                        if (!r.isSuccess()) {
                            Utilities.mostrarMensaje("Error al guardar", r.getMensaje());
                            return;
                        }

                        // Crear nuevo StackPane
                        StackPane nuevoStack = crearCeldaEspacio(espacioSeleccionado);
                        gridMatrix.add(nuevoStack, finalCol, finalRow, colSpan, rowSpan);

                        // Actualizar vista
                        llenarCeldasLibres();

                        // Limpiar selección
                        espacioSeleccionado = null;
                        stackSeleccionado = null;

                        event.consume();
                    }
                });

                gridMatrix.add(celdaLibre, col, row);
            }
        }
    }
}

    private void cargarContadoresPorPiso(int piso) {
        LabelCanEscritorios.setText(String.valueOf(escritoriosPorPiso.getOrDefault(piso, 0)));
        LabelCanSalasComunes.setText(String.valueOf(salasPorPiso.getOrDefault(piso, 0)));
        LabelCantAreasComunes.setText(String.valueOf(areasPorPiso.getOrDefault(piso, 0)));
        LabelCantEspaciosLibres.setText(String.valueOf(libresPorPiso.getOrDefault(piso, 0)));
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
actualizarCapacidadTotalDelPiso();
    }
private void actualizarCapacidadTotalDelPiso() {
    int escritorios = escritoriosPorPiso.getOrDefault(pisoActual, 0);
    int salas = salasPorPiso.getOrDefault(pisoActual, 0);
    int areas = areasPorPiso.getOrDefault(pisoActual, 0);
    int capacidadTotal = escritorios * 1 + salas * 4 + areas * 2;
    LabelCapacidadTotal.setText("Capacidad total: " + capacidadTotal);

    // Si deseas guardar en la BD:
    spacesService.actualizarCapacidadCoworkingSpace(pisoActual, capacidadTotal);
}



private void restaurarPosicionOriginal(StackPane stack, SpaceVisual espacio) {
    if (stack.getParent() != null) {
        ((Pane) stack.getParent()).getChildren().remove(stack);
    }
    gridMatrix.add(stack, espacio.getColumn(), espacio.getRow(), espacio.getColSpan(), espacio.getRowSpan());
}

private void resaltarTemporal(StackPane celda) {
    // Guarda el estilo original
    String estiloOriginal = celda.getStyle();

    // Aplica borde amarillo
    celda.setStyle("-fx-border-color: yellow; -fx-border-width: 3; -fx-border-radius: 5;");

    // Programar volver al estilo original luego de 1.5 segundos
    Timer timer = new Timer();
    timer.schedule(new TimerTask() {
        @Override
        public void run() {
            javafx.application.Platform.runLater(() -> celda.setStyle(estiloOriginal));
        }
    }, 1500); // 1.5 segundos
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
          resaltarTemporal(celda);
          actualizarCapacidadTotalDelPiso();
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
          resaltarTemporal(celda);
          actualizarCapacidadTotalDelPiso();
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
          resaltarTemporal(celda);
          actualizarCapacidadTotalDelPiso();
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
          resaltarTemporal(celda);
          actualizarCapacidadTotalDelPiso();
    } else {
        System.out.println("No hay espacio para más espacios libres.");
    }
}

    @FXML
 private void onBorrarTodo() {
    // Paso 1: Confirmación visual
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Confirmación");
    alert.setHeaderText("¿Deseas borrar TODOS los espacios del piso " + pisoActual + "?");
    alert.setContentText("Esta acción no se puede deshacer.");

    Optional<ButtonType> result = alert.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {

        // Paso 2: Verificar si hay espacios en este piso
        List<SpaceVisual> espacios = spacesService.obtenerEspaciosConPosicion();
        String filtro = "P" + pisoActual;
        boolean hayEspacios = espacios.stream()
            .anyMatch(esp -> esp.getSpace().getNombre().contains(filtro));

        if (!hayEspacios) {
            Alert info = new Alert(Alert.AlertType.INFORMATION);
            info.setTitle("Información");
            info.setHeaderText("No hay espacios para borrar");
            info.setContentText("El piso " + pisoActual + " no tiene espacios registrados.");
            info.showAndWait();
            return;
        }

        // Paso 3: Eliminar solo los del piso actual
        Respuesta resp = spacesService.eliminarEspaciosPorPiso(pisoActual);
        if (resp.isSuccess()) {
            escritoriosPorPiso.put(pisoActual, 0);
            salasPorPiso.put(pisoActual, 0);
            areasPorPiso.put(pisoActual, 0);
            libresPorPiso.put(pisoActual, 0);
            espaciosAgregados.removeIf(esp -> esp.getSpace().getNombre().contains(filtro));

            LabelCanEscritorios.setText("0");
            LabelCanSalasComunes.setText("0");
            LabelCantAreasComunes.setText("0");
            LabelCantEspaciosLibres.setText("0");

            gridMatrix.getChildren().clear();
            cargarMatrizConEspacios();
        } else {
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setTitle("Error");
            error.setHeaderText("No se pudieron eliminar los espacios");
            error.setContentText(resp.getMensaje());
            error.showAndWait();
        }
    }
}
private void editarEspacio(SpaceVisual espacio) {
    TextInputDialog dialog = new TextInputDialog(espacio.getSpace().getNombre());
    dialog.setTitle("Editar espacio");
    dialog.setHeaderText("Modificar nombre del espacio");
    dialog.setContentText("Nuevo nombre:");

    Optional<String> result = dialog.showAndWait();
    result.ifPresent(nombreNuevo -> {
        if (!nombreNuevo.trim().isEmpty()) {
            // Actualizar el nombre en la base de datos
            SpacesDto dto = espacio.getSpace();
            dto.setNombre(nombreNuevo.trim());

            Respuesta respuesta = spacesService.guardarSpace(dto);
            if (respuesta.isSuccess()) {
                cargarMatrizConEspacios(); // Recargar la vista con el nuevo nombre
            } else {
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setTitle("Error");
                error.setHeaderText("No se pudo actualizar el espacio");
                error.setContentText(respuesta.getMensaje());
                error.showAndWait();
            }
        }
    });
}
    @Override
    public void initialize() {
      
    }
}
