package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.DTO.SpacesDto;
import cr.ac.una.proyecto1progra2.model.Spaces;
import cr.ac.una.proyecto1progra2.service.SpacesService;
import cr.ac.una.proyecto1progra2.util.Respuesta;
import cr.ac.una.proyecto1progra2.util.SpaceVisual;
import cr.ac.una.proyecto1progra2.util.Utilities;
import javafx.scene.media.AudioClip;

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
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import oracle.net.aso.c;

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
    private final List<Rectangle> celdasDisponibles = new ArrayList<>();

 private static final double CELL_WIDTH = 100;
private static final double CELL_HEIGHT = 70;

    private Map<Integer, Integer> escritoriosPorPiso = new HashMap<>();
    private Map<Integer, Integer> salasPorPiso = new HashMap<>();
    private Map<Integer, Integer> areasPorPiso = new HashMap<>();
    private Map<Integer, Integer> libresPorPiso = new HashMap<>();
private final List<Rectangle> celdasResaltadas = new ArrayList<>();
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        gridMatrix.getStyleClass().add("grid-background");
        gridMatrix.setHgap(20); // separación horizontal entre celdas
gridMatrix.setVgap(20); // separación vertical entre celdas
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
        activarClickFueraParaCancelarSeleccion();
        
        
    }
    private boolean intersecta(int r1, int c1, int rs1, int cs1, int r2, int c2, int rs2, int cs2) {
    return r1 < r2 + rs2 && r1 + rs1 > r2 && c1 < c2 + cs2 && c1 + cs1 > c2;
   
}

public StackPane crearCeldaEspacio(SpaceVisual espacio) {
    StackPane stack = new StackPane();
    stack.getStyleClass().add("stack-celda");

    Rectangle rect = new Rectangle(CELL_WIDTH * espacio.getColSpan(), CELL_HEIGHT * espacio.getRowSpan());
    rect.setArcWidth(40);
    rect.setArcHeight(40);
    rect.setStroke(Color.TRANSPARENT);  // 🔸 Quita el borde negro
    rect.getStyleClass().add("celda-rect");

    String nombre = espacio.getSpace().getNombre().toLowerCase(Locale.ROOT);
    if (nombre.contains("sala")) rect.setFill(Color.web("#2196F3"));
    else if (nombre.contains("área")) rect.setFill(Color.web("#9C27B0"));
    else if (nombre.contains("libre")) rect.setFill(Color.web("#4CAF50")); // gris oscuro
    else if (nombre.contains("e")) rect.setFill(Color.web("#FF5722"));
    else rect.setFill(Color.LIGHTGRAY);

Text text = new Text(espacio.getSpace().getNombre());
text.setFill(Color.WHITE);
text.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

    stack.getChildren().addAll(rect, text);

    stack.setOnMouseClicked(event -> {
        limpiarCeldasDisponibles();
        limpiarCuadrosDisponibles();
        if (stackSeleccionado != null) stackSeleccionado.setStyle("");
        espacioSeleccionado = espacio;
        stackSeleccionado = stack;
        stack.setStyle("-fx-border-color: yellow; -fx-border-width: 3; -fx-border-radius: 8;");
        mostrarCeldasDisponibles(espacioSeleccionado);

        event.consume();
    });

    return stack;
}
private void activarClickFueraParaCancelarSeleccion() {
    rootPane.setOnMouseClicked(event -> {
        // Si no hiciste clic sobre el gridMatrix directamente
        if (!event.getTarget().toString().contains("gridMatrix")) {
            if (espacioSeleccionado != null) {
                limpiarCeldasDisponibles();
                limpiarCuadrosDisponibles();
                if (stackSeleccionado != null) {
                    stackSeleccionado.setStyle(""); // quitar borde
                }
                espacioSeleccionado = null;
                stackSeleccionado = null;
            }
        }
    });
}


private void reproducirSonido(String nombreArchivo) {
    try {
        AudioClip sonido = new AudioClip(getClass().getResource("/cr/ac/una/proyecto1progra2/resources/" + nombreArchivo).toExternalForm());
        sonido.play();
    } catch (Exception e) {
        System.out.println("Error al reproducir sonido: " + e.getMessage());
    }
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
               celdaLibre.setStyle("-fx-background-color: transparent;");
                celdaLibre.setUserData("celdaLibre"); // ⚠️ importante para buscarlas luego
                final int finalRow = row;
                final int finalCol = col;

                celdaLibre.setOnMouseClicked(event -> {
                    if (espacioSeleccionado != null && stackSeleccionado != null) {
                        limpiarCeldasDisponibles();
                        int rowSpan = espacioSeleccionado.getRowSpan();
                        int colSpan = espacioSeleccionado.getColSpan();

                        boolean dentro = finalCol + colSpan <= 4 && finalRow + rowSpan <= 4;
                        if (!dentro) {
                            Utilities.mostrarMensaje("Posición inválida", "El espacio no cabe en esa posición.");
                            return;
                        }

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

                        gridMatrix.getChildren().remove(stackSeleccionado);
                        espacioSeleccionado.setRow(finalRow);
                        espacioSeleccionado.setColumn(finalCol);
                        SpacesDto dto = espacioSeleccionado.getSpace();
                        dto.setRow(finalRow);
                        dto.setColumn(finalCol);
                        Respuesta r = spacesService.guardarSpace(dto);
                        if (!r.isSuccess()) {
                            Utilities.mostrarMensaje("Error al guardar", r.getMensaje());
                            return;
                        }

                        StackPane nuevoStack = crearCeldaEspacio(espacioSeleccionado);
                        gridMatrix.add(nuevoStack, finalCol, finalRow, colSpan, rowSpan);

                        espacioSeleccionado = null;
                        stackSeleccionado = null;
                        llenarCeldasLibres();
                        event.consume();
                    }
                });
                gridMatrix.add(celdaLibre, col, row);
            }
        }
    }
}

private void limpiarCeldasDisponibles() {
    List<Node> nodosParaEliminar = new ArrayList<>();

    for (Node node : gridMatrix.getChildren()) {
        if (node instanceof StackPane) {
            StackPane celda = (StackPane) node;
            if (!celda.getChildren().isEmpty() && celda.getChildren().get(0) instanceof Rectangle) {
                Rectangle fondo = (Rectangle) celda.getChildren().get(0);
                if (fondo.getFill().equals(Color.rgb(255, 255, 0, 0.4))) {
                    FadeTransition fadeOut = new FadeTransition(Duration.millis(250), celda);
                    fadeOut.setFromValue(1.0);
                    fadeOut.setToValue(0.0);
                    fadeOut.setOnFinished(e -> gridMatrix.getChildren().remove(celda));
                    fadeOut.play();
                    nodosParaEliminar.add(celda);
                }
            }
        }
    }
}

private void mostrarCeldasDisponibles(SpaceVisual espacio) {
    limpiarCeldasDisponibles();
    int filas = 4;
    int columnas = 4;
    int spanRow = espacio.getRowSpan();
    int spanCol = espacio.getColSpan();

    for (int row = 0; row <= filas - spanRow; row++) {
        for (int col = 0; col <= columnas - spanCol; col++) {
            boolean ocupado = false;
            for (SpaceVisual ocupadoEspacio : espaciosAgregados) {
                if (ocupadoEspacio == espacioSeleccionado) continue;
                if (intersecta(row, col, spanRow, spanCol,
                        ocupadoEspacio.getRow(),
                        ocupadoEspacio.getColumn(),
                        ocupadoEspacio.getRowSpan(),
                        ocupadoEspacio.getColSpan())) {
                    ocupado = true;
                    break;
                }
            }

            if (!ocupado) {
                StackPane celda = new StackPane();
                celda.setPrefSize(CELL_WIDTH * spanCol, CELL_HEIGHT * spanRow);

                Rectangle fondo = new Rectangle(CELL_WIDTH * spanCol, CELL_HEIGHT * spanRow);
                fondo.setFill(Color.rgb(255, 255, 0, 0.4)); // Amarillo con transparencia
                fondo.setStroke(Color.YELLOW);
                fondo.setArcWidth(10);
                fondo.setArcHeight(10);
                celda.getChildren().add(fondo);

                // Posicionamiento
                GridPane.setRowIndex(celda, row);
                GridPane.setColumnIndex(celda, col);
                GridPane.setRowSpan(celda, spanRow);
                GridPane.setColumnSpan(celda, spanCol);

                // Animación de entrada
                celda.setOpacity(0);
                celda.setScaleX(0.8);
                celda.setScaleY(0.8);

                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), celda);
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1);

                ScaleTransition scaleIn = new ScaleTransition(Duration.millis(300), celda);
                scaleIn.setFromX(0.8);
                scaleIn.setFromY(0.8);
                scaleIn.setToX(1.0);
                scaleIn.setToY(1.0);

                ParallelTransition animacion = new ParallelTransition(fadeIn, scaleIn);
                animacion.play();

                final int finalRow = row;
                final int finalCol = col;
                celda.setOnMouseClicked(event -> {
                    if (espacioSeleccionado != null && stackSeleccionado != null) {
                        gridMatrix.getChildren().remove(stackSeleccionado);
                        espacioSeleccionado.setRow(finalRow);
                        espacioSeleccionado.setColumn(finalCol);
                        espacioSeleccionado.getSpace().setRow(finalRow);
                        espacioSeleccionado.getSpace().setColumn(finalCol);
                        Respuesta r = spacesService.guardarSpace(espacioSeleccionado.getSpace());
                        if (!r.isSuccess()) {
                            Utilities.mostrarMensaje("Error al guardar", r.getMensaje());
                            return;
                        }
                        espacioSeleccionado = null;
                        stackSeleccionado = null;
                        cargarMatrizConEspacios();
                    }
                    event.consume();
                });

                gridMatrix.getChildren().add(celda);
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

    // Reiniciar contadores
    escritoriosPorPiso.put(pisoActual, 0);
    salasPorPiso.put(pisoActual, 0);
    areasPorPiso.put(pisoActual, 0);
    libresPorPiso.put(pisoActual, 0);

    // Obtener espacios del piso actual
    List<SpaceVisual> espacios = spacesService.obtenerEspaciosConPosicion()
            .stream()
            .filter(e -> e.getSpace().getNombre().contains("P" + pisoActual))
            .collect(Collectors.toList());

    for (SpaceVisual espacio : espacios) {
        espaciosAgregados.add(espacio);
        StackPane celda = crearCeldaEspacio(espacio);
        gridMatrix.add(celda, espacio.getColumn(), espacio.getRow(), espacio.getColSpan(), espacio.getRowSpan());

        // Contar por tipo (según prefijo del nombre)
        String nombre = espacio.getSpace().getNombre().toLowerCase(Locale.ROOT);
        if (nombre.startsWith("e -")) {
            escritoriosPorPiso.put(pisoActual, escritoriosPorPiso.get(pisoActual) + 1);
        } else if (nombre.startsWith("s -")) {
            salasPorPiso.put(pisoActual, salasPorPiso.get(pisoActual) + 1);
        } else if (nombre.startsWith("a -")) {
            areasPorPiso.put(pisoActual, areasPorPiso.get(pisoActual) + 1);
        } else if (nombre.startsWith("l -")) {
            libresPorPiso.put(pisoActual, libresPorPiso.get(pisoActual) + 1);
        }
    }

    cargarContadoresPorPiso(pisoActual);
    llenarCeldasLibres();
    actualizarCapacidadTotalDelPiso();
}
   
    private void limpiarCuadrosDisponibles() {
    for (Node node : gridMatrix.getChildren()) {
        if (node instanceof StackPane) {
            StackPane stack = (StackPane) node;
            for (Node child : stack.getChildren()) {
                if (child instanceof Rectangle) {
                    Rectangle rect = (Rectangle) child;
                    if (rect.getStroke() != null && rect.getStroke().equals(Color.YELLOW)) {
                        rect.setStroke(Color.BLACK);
                        rect.setStrokeWidth(1);
                    }
                }
            }
        }
    }
}
    
    
    private boolean verificarEspacioDisponible(int filaInicio, int colInicio, int rowSpan, int colSpan) {
    for (SpaceVisual esp : espaciosAgregados) {
        int r = esp.getRow();
        int c = esp.getColumn();
        int rs = esp.getRowSpan();
        int cs = esp.getColSpan();
        if (filaInicio < r + rs && filaInicio + rowSpan > r &&
            colInicio < c + cs && colInicio + colSpan > c) {
            return false; // hay superposición
        }
    }
    return true; // está libre
}
    
    private void configurarMovimientoPorClic() {
    gridMatrix.setOnMouseClicked(event -> {
        if (espacioSeleccionado != null) {
            Point2D coords = gridMatrix.sceneToLocal(event.getSceneX(), event.getSceneY());
            int col = (int) (coords.getX() / CELL_WIDTH);
            int row = (int) (coords.getY() / CELL_HEIGHT);

            boolean valido = verificarEspacioDisponible(row, col, 
                              espacioSeleccionado.getRowSpan(), espacioSeleccionado.getColSpan());

            if (valido) {
                gridMatrix.getChildren().removeIf(n ->
                    GridPane.getRowIndex(n) != null && GridPane.getColumnIndex(n) != null &&
                    GridPane.getRowIndex(n) == espacioSeleccionado.getRow() &&
                    GridPane.getColumnIndex(n) == espacioSeleccionado.getColumn()
                );

                StackPane nuevoStack = crearCeldaEspacio(espacioSeleccionado);
                espacioSeleccionado.setRow(row);
                espacioSeleccionado.setColumn(col);
                gridMatrix.add(nuevoStack, col, row, 
                               espacioSeleccionado.getColSpan(), espacioSeleccionado.getRowSpan());
            }

            // 🔸 Siempre limpiar los bordes amarillos al terminar
            limpiarCuadrosDisponibles();

            espacioSeleccionado = null;
            event.consume();
        } else {
            // 🔸 Si no hay espacio seleccionado, también limpiar los cuadros
            limpiarCuadrosDisponibles();
        }
    });
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
   

    @FXML
   private void onAgregarEscritorios() {
    int current = escritoriosPorPiso.getOrDefault(pisoActual, 0);
    SpaceVisual nuevo = crearEspacioLibreConSpan("E - Escritorio " + (current + 1), 1, 1);
    if (nuevo != null) {
        escritoriosPorPiso.put(pisoActual, current + 1);
        LabelCanEscritorios.setText(String.valueOf(current + 1));
        espaciosAgregados.add(nuevo);
        StackPane celda = crearCeldaEspacio(nuevo);
        gridMatrix.add(celda, nuevo.getColumn(), nuevo.getRow(), nuevo.getColSpan(), nuevo.getRowSpan());
        resaltarTemporal(celda);
        actualizarCapacidadTotalDelPiso();
        reproducirSonido("mech-keyboard-02-102918.wav");
    }
}

    @FXML
   private void onAgregarSalas() {
    int current = salasPorPiso.getOrDefault(pisoActual, 0);
    SpaceVisual nuevo = crearEspacioLibreConSpan("S - Sala " + (current + 1), 2, 2);
    if (nuevo != null) {
        salasPorPiso.put(pisoActual, current + 1);
        LabelCanSalasComunes.setText(String.valueOf(current + 1));
        espaciosAgregados.add(nuevo);
        StackPane celda = crearCeldaEspacio(nuevo);
        gridMatrix.add(celda, nuevo.getColumn(), nuevo.getRow(), nuevo.getColSpan(), nuevo.getRowSpan());
        resaltarTemporal(celda);
        actualizarCapacidadTotalDelPiso();
        reproducirSonido("mech-keyboard-02-102918.wav");
    }
}
    
private boolean validarNombreDisponible(String nombre) {
    EntityManager em = Persistence.createEntityManagerFactory("ProyectoPU").createEntityManager();
    try {
        TypedQuery<Spaces> q = em.createNamedQuery("Spaces.findByName", Spaces.class);
        q.setParameter("name", nombre.toUpperCase());
        return q.getResultList().isEmpty(); // true = disponible
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    } finally {
        em.close();
    }
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

                boolean seCruzan = row < r + rs && row + rowSpan > r &&
                                   col < c + cs && col + colSpan > c;

                System.out.println("Probando posicion fila: " + row + " columna: " + col + " para span " + rowSpan + "x" + colSpan);

                if (seCruzan) {
                    System.out.println("🚫 Se cruza con: " + esp.getSpace().getNombre() + " en fila: " + r + " col: " + c);
                    ocupado = true;
                    break;
                }
            }

            if (!ocupado) {
                // 🔁 Generar nombre único si ya existe
                String base = nombre + " P" + pisoActual;
                String nombreFinal = base;
                int sufijo = 1;
                while (!validarNombreDisponible(nombreFinal)) {
                    nombreFinal = base + " (" + sufijo + ")";
                    sufijo++;
                }

                SpacesDto nuevo = new SpacesDto();
                nuevo.setNombre(nombreFinal);
                nuevo.setRow(row);
                nuevo.setColumn(col);
                nuevo.setRowSpan(rowSpan);
                nuevo.setColSpan(colSpan);

                Respuesta r = spacesService.guardarSpace(nuevo);
                if (!r.getEstado()) {
                    System.out.println("❌ Error al guardar: " + r.getMensaje());
                    return null;
                }

                SpacesDto actualizado = (SpacesDto) spacesService
                        .getSpace(((SpacesDto) r.getResultado("Space")).getId())
                        .getResultado("Space");

                System.out.println("✅ Espacio agregado: " + nombreFinal + " en fila: " + row + ", col: " + col);
                return new SpaceVisual(actualizado, row, col, rowSpan, colSpan);
            }
        }
    }

    System.out.println("❌ No se encontró ninguna posición libre para " + nombre + " (" + rowSpan + "x" + colSpan + ")");
    return null;
}
private void imprimirGrillaActual() {
    String[][] grilla = new String[4][4];
    for (int i = 0; i < 4; i++) {
        Arrays.fill(grilla[i], " . "); // puntos representan espacios vacíos
    }

    for (SpaceVisual esp : espaciosAgregados) {
        String etiqueta = esp.getSpace().getNombre().substring(0, 1).toUpperCase(); // S, E, L, A
        for (int i = 0; i < esp.getRowSpan(); i++) {
            for (int j = 0; j < esp.getColSpan(); j++) {
                int r = esp.getRow() + i;
                int c = esp.getColumn() + j;
                if (r < 4 && c < 4) grilla[r][c] = " " + etiqueta + " ";
            }
        }
    }

    System.out.println("📐 Estado actual del grid:");
    for (int r = 0; r < 4; r++) {
        for (int c = 0; c < 4; c++) {
            System.out.print(grilla[r][c]);
        }
        System.out.println();
    }
    System.out.println("--------------------------------");
}
    @FXML
private void onAgregarAreasComunes() {
    int current = areasPorPiso.getOrDefault(pisoActual, 0);
    SpaceVisual nuevo = crearEspacioLibreConSpan("A - Área " + (current + 1), 1, 1);
    if (nuevo != null) {
        areasPorPiso.put(pisoActual, current + 1);
        LabelCantAreasComunes.setText(String.valueOf(current + 1));
        espaciosAgregados.add(nuevo);
        StackPane celda = crearCeldaEspacio(nuevo);
        gridMatrix.add(celda, nuevo.getColumn(), nuevo.getRow(), nuevo.getColSpan(), nuevo.getRowSpan());
        resaltarTemporal(celda);
        actualizarCapacidadTotalDelPiso();
        reproducirSonido("mech-keyboard-02-102918.wav");
    }
}

    @FXML
   private void onAgregarEspaciosLibres() {
    int current = libresPorPiso.getOrDefault(pisoActual, 0);
    SpaceVisual nuevo = crearEspacioLibreConSpan("L - Libre " + (current + 1), 1, 1);
    if (nuevo != null) {
        libresPorPiso.put(pisoActual, current + 1);
        LabelCantEspaciosLibres.setText(String.valueOf(current + 1));
        espaciosAgregados.add(nuevo);
        StackPane celda = crearCeldaEspacio(nuevo);
        gridMatrix.add(celda, nuevo.getColumn(), nuevo.getRow(), nuevo.getColSpan(), nuevo.getRowSpan());
        resaltarTemporal(celda);
        actualizarCapacidadTotalDelPiso();
        reproducirSonido("mech-keyboard-02-102918.wav");
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
