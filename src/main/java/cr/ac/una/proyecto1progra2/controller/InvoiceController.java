package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.DTO.UsuariosDto;
import cr.ac.una.proyecto1progra2.model.CoworkingSpaces;
import cr.ac.una.proyecto1progra2.service.ReservationsService;
import cr.ac.una.proyecto1progra2.service.SpacesService;
import cr.ac.una.proyecto1progra2.util.FlowController;
import cr.ac.una.proyecto1progra2.util.UserManager;
import cr.ac.una.proyecto1progra2.util.Utilities;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import javafx.scene.media.AudioClip;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;
import javafx.application.Platform;
import javafx.stage.Stage;


public class InvoiceController extends Controller implements Initializable {
    
    @FXML private VBox mainContainer;
    @FXML private StackPane headerContainer;
    @FXML private VBox detailsContainer;
    @FXML private HBox footerContainer;  
    @FXML private Button btnClose;
    
    private LocalDate fechaReserva;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private int pisoReservado;
    private List<CoworkingSpaces> espaciosReservados;
    private double totalAmount;
    private String numeroFactura;
    
    private ReservationsService reservationsService = new ReservationsService();
    private SpacesService spacesService = new SpacesService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupInitialState();
        createInvoiceLayout();
        startAnimations();
    }
    
   public void setReservationData(LocalDate fecha, LocalTime inicio, LocalTime fin, 
                               int piso, List<CoworkingSpaces> espacios) {
    this.fechaReserva = fecha;
    this.horaInicio = inicio;
    this.horaFin = fin;
    this.pisoReservado = piso;
    this.espaciosReservados = espacios;
    this.numeroFactura = generateInvoiceNumber();


    // Crear y mostrar la factura con los datos ya cargados
    Platform.runLater(() -> {
        detailsContainer.getChildren().clear();
        createInvoiceLayout();
        startAnimations();
    });
}
    
    private void setupInitialState() {
        mainContainer.setOpacity(0);
        mainContainer.setTranslateY(50);
        
        // Configurar estilos base
        mainContainer.setStyle(
            "-fx-background-color: linear-gradient(to bottom, #f8f9fa, #e9ecef);" +
            "-fx-background-radius: 15;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 20, 0, 0, 5);"
        );
        
        setupButtonStyles();
    }
    
    private void setupButtonStyles() {
       
        styleButton(btnClose, "#6c757d", "#545b62");
    }
    
    private void styleButton(Button button, String baseColor, String hoverColor) {
        button.setStyle(
            "-fx-background-color: " + baseColor + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 25;" +
            "-fx-padding: 12 25;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 5, 0, 0, 2);"
        );
        
        button.setOnMouseEntered(e -> {
            button.setStyle(button.getStyle().replace(baseColor, hoverColor));
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), button);
            scale.setToX(1.05);
            scale.setToY(1.05);
            scale.play();
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(button.getStyle().replace(hoverColor, baseColor));
            ScaleTransition scale = new ScaleTransition(Duration.millis(100), button);
            scale.setToX(1.0);
            scale.setToY(1.0);
            scale.play();
        });
    }
    
    private void createInvoiceLayout() {
        createHeader();
        createCompanyInfo();
        createInvoiceDetails();
        createItemsTable();

        createFooterInfo();
    }
    
    private void createHeader() {
        headerContainer.getChildren().clear();
        
        // Fondo con gradiente
        Rectangle headerBg = new Rectangle(600, 120);
        LinearGradient gradient = new LinearGradient(0, 0, 1, 1, true, null,
            new Stop(0, Color.web("#667eea")),
            new Stop(1, Color.web("#764ba2"))
        );
        headerBg.setFill(gradient);
        headerBg.setArcWidth(15);
        headerBg.setArcHeight(15);
        
        // Título principal
        Text title = new Text("COMPROBANTE DE RESERVA");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setFill(Color.WHITE);
        title.setTextAlignment(TextAlignment.CENTER);
        
        // Número de factura
        Text invoiceNum = new Text("N° " + (numeroFactura != null ? numeroFactura : "COMPROBANTE-001"));
        invoiceNum.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        invoiceNum.setFill(Color.web("#e8eaf6"));
        
        VBox headerText = new VBox(5);
        headerText.setAlignment(Pos.CENTER);
        headerText.getChildren().addAll(title, invoiceNum);
        
        headerContainer.getChildren().addAll(headerBg, headerText);
    }
    
    private void createCompanyInfo() {
        VBox companyBox = new VBox(8);
        companyBox.setPadding(new Insets(20, 30, 10, 30));
        
        // Logo y nombre de empresa
        HBox logoRow = new HBox(15);
        logoRow.setAlignment(Pos.CENTER_LEFT);
        
        Rectangle logo = new Rectangle(60, 60);
        logo.setFill(Color.web("#4CAF50"));
        logo.setArcWidth(10);
        logo.setArcHeight(10);
        
        Text logoText = new Text("FS");
        logoText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        logoText.setFill(Color.WHITE);
        
        StackPane logoStack = new StackPane(logo, logoText);
        
        VBox companyDetails = new VBox(3);
        Text companyName = createStyledText("FlexSpace Coworking", 22, FontWeight.BOLD, "#2c3e50");
        Text companyAddress = createStyledText("Perez Zeledon, San Jose, Costa Rica", 12, FontWeight.NORMAL, "#6c757d");
        Text companyContact = createStyledText("Tel: +506 2445-5000 | Email: info@flexspace.cr", 12, FontWeight.NORMAL, "#6c757d");
        
        companyDetails.getChildren().addAll(companyName, companyAddress, companyContact);
        logoRow.getChildren().addAll(logoStack, companyDetails);
        
        // Línea separadora animada
        Line separator = new Line(0, 0, 500, 0);
        separator.setStroke(Color.web("#dee2e6"));
        separator.setStrokeWidth(2);
        
        companyBox.getChildren().addAll(logoRow, separator);
        detailsContainer.getChildren().add(companyBox);
    }
    
    private void createInvoiceDetails() {
        HBox detailsRow = new HBox(40);
        detailsRow.setPadding(new Insets(20, 30, 20, 30));
        detailsRow.setAlignment(Pos.TOP_LEFT);
        
        // Información del cliente
        VBox clientInfo = createInfoBox("INFORMACIÓN DEL CLIENTE", Color.web("#e3f2fd"));
        UsuariosDto user = UserManager.getCurrentUser();
        if (user != null) {
            clientInfo.getChildren().addAll(
                createInfoRow("Cliente:", user.getNombre() ),
              
                createInfoRow("ID:", user.getId().toString())
            );
        }
        
        // Información de la reserva
        VBox reservationInfo = createInfoBox("DETALLES DE LA RESERVA", Color.web("#f3e5f5"));
        reservationInfo.getChildren().addAll(
            createInfoRow("Fecha:", fechaReserva != null ? fechaReserva.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A"),
            createInfoRow("Hora Inicio:", horaInicio != null ? horaInicio.toString() : "N/A"),
            createInfoRow("Hora Fin:", horaFin != null ? horaFin.toString() : "N/A"),
            createInfoRow("Piso:", "Piso " + pisoReservado)
        );
        
        detailsRow.getChildren().addAll(clientInfo, reservationInfo);
        detailsContainer.getChildren().add(detailsRow);
    }
    
    private VBox createInfoBox(String title, Color bgColor) {
        VBox box = new VBox(10);
        box.setPadding(new Insets(15));
        box.setStyle(
            "-fx-background-color: " + toRGBCode(bgColor) + ";" +
            "-fx-background-radius: 8;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 3, 0, 0, 1);"
        );
        box.setPrefWidth(250);
        
        Text titleText = new Text(title);
        titleText.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        titleText.setFill(Color.web("#495057"));
        
        box.getChildren().add(titleText);
        return box;
    }
    
    private HBox createInfoRow(String label, String value) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        
        Text labelText = createStyledText(label, 12, FontWeight.BOLD, "#6c757d");
        Text valueText = createStyledText(value, 12, FontWeight.NORMAL, "#212529");
        
        row.getChildren().addAll(labelText, valueText);
        return row;
    }
    
    private void createItemsTable() {
        VBox tableContainer = new VBox(0);
        tableContainer.setPadding(new Insets(0, 30, 0, 30));
        
        // Encabezado de tabla
        HBox tableHeader = new HBox();
        tableHeader.setPadding(new Insets(15));
        tableHeader.setStyle(
            "-fx-background-color: linear-gradient(to right, #667eea, #764ba2);" +
            "-fx-background-radius: 8 8 0 0;"
        );
        
        Text[] headers = {
            createTableHeaderText("ESPACIO  ", 200),
            
            
    
        };
        
        tableHeader.getChildren().addAll(headers);
        
        // Contenido de tabla
        VBox tableContent = new VBox();
        tableContent.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 0 0 8 8;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);"
        );
        
        if (espaciosReservados != null) {
            for (int i = 0; i < espaciosReservados.size(); i++) {
                CoworkingSpaces espacio = espaciosReservados.get(i);
                HBox row = createTableRow(espacio, i % 2 == 0);
                tableContent.getChildren().add(row);
            }
        }
        
        tableContainer.getChildren().addAll(tableHeader, tableContent);
        detailsContainer.getChildren().add(tableContainer);
    }
    
    private Text createTableHeaderText(String text, double width) {
        Text headerText = new Text(text);
        headerText.setFont(Font.font("Arial  ", FontWeight.BOLD, 12));
        headerText.setFill(Color.WHITE);
        
        Region spacer = new Region();
        spacer.setPrefWidth(width - text.length() * 8);
        
        return headerText;
    }
    
    private HBox createTableRow(CoworkingSpaces espacio, boolean isEven) {
        HBox row = new HBox();
        row.setPadding(new Insets(12, 15, 12, 15));
        row.setAlignment(Pos.CENTER_LEFT);
        
        if (isEven) {
            row.setStyle("-fx-background-color: #f8f9fa;");
        }
        
        // Obtener el nombre real del espacio desde SpaceId
String nombreEspacio = espacio.getSpaceId().getName(); // ← Aquí accedés al nombre
String tipoEspacio = determineSpaceType(nombreEspacio);  // ← Determina si es Escritorio, Sala, etc.

Text[] cells = {
    createTableCellText(tipoEspacio, 200),
};// Datos del espacio (simulados ya que no tienes la estructura completa)
        
        
        row.getChildren().addAll(cells);
        
        // Animación de entrada
        row.setOpacity(0);
        row.setTranslateX(-50);
        
        FadeTransition fade = new FadeTransition(Duration.millis(500), row);
        fade.setToValue(1);
        TranslateTransition slide = new TranslateTransition(Duration.millis(500), row);
        slide.setToX(0);
        
        ParallelTransition parallel = new ParallelTransition(fade, slide);
        parallel.setDelay(Duration.millis(100 * detailsContainer.getChildren().size()));
        parallel.play();
        
        return row;
    }
    
    private Text createTableCellText(String text, double width) {
        Text cellText = new Text(text);
        cellText.setFont(Font.font("Arial", FontWeight.NORMAL, 11));
        cellText.setFill(Color.web("#495057"));
        return cellText;
    }
    
    
    
    
    private void createFooterInfo() {
        VBox footerBox = new VBox(10);
        footerBox.setPadding(new Insets(20, 30, 30, 30));
        footerBox.setAlignment(Pos.CENTER);
        
        Text thankYou = createStyledText("¡Gracias por elegir FlexSpace!", 16, FontWeight.BOLD, "#2c3e50");
        Text footerNote = createStyledText("Este comprobante fue generado automáticamente el " + 
                                         LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), 
                                         10, FontWeight.NORMAL, "#6c757d");
        
        footerBox.getChildren().addAll(thankYou, footerNote);
        detailsContainer.getChildren().add(footerBox);
    }
    
    private void startAnimations() {
        // Animación principal de entrada
        FadeTransition mainFade = new FadeTransition(Duration.millis(800), mainContainer);
        mainFade.setToValue(1);
        
        TranslateTransition mainSlide = new TranslateTransition(Duration.millis(800), mainContainer);
        mainSlide.setToY(0);
        mainSlide.setInterpolator(Interpolator.EASE_OUT);
        
        ParallelTransition mainAnimation = new ParallelTransition(mainFade, mainSlide);
        mainAnimation.play();
        
        // Animaciones de botones
        animateButtons();
    }
    
    private void animateButtons() {
        Button[] buttons = { btnClose};
        
        for (int i = 0; i < buttons.length; i++) {
            Button btn = buttons[i];
            btn.setOpacity(0);
            btn.setScaleX(0.8);
            btn.setScaleY(0.8);
            
            FadeTransition fade = new FadeTransition(Duration.millis(400), btn);
            fade.setToValue(1);
            
            ScaleTransition scale = new ScaleTransition(Duration.millis(400), btn);
            scale.setToX(1);
            scale.setToY(1);
            scale.setInterpolator(Interpolator.EASE_BOTH);
            
            ParallelTransition btnAnimation = new ParallelTransition(fade, scale);
            btnAnimation.setDelay(Duration.millis(1000 + (i * 100)));
            btnAnimation.play();
        }
    }
    
    // Métodos de utilidad
    private Text createStyledText(String content, double size, FontWeight weight, String color) {
        Text text = new Text(content);
        text.setFont(Font.font("Arial", weight, size));
        text.setFill(Color.web(color));
        return text;
    }
    
    private String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
            (int) (color.getRed() * 255),
            (int) (color.getGreen() * 255),
            (int) (color.getBlue() * 255));
    }
    
    private String generateInvoiceNumber() {
        return "FAC-" + String.format("%06d", ThreadLocalRandom.current().nextInt(1, 999999));
    }
    
    
    
private double calculateHours() {
    if (horaInicio == null || horaFin == null) return 1.0;
    return java.time.Duration.between(horaInicio, horaFin).toMinutes() / 60.0;
}
    
    private String determineSpaceType(String espacioNombre) {
        String lower = espacioNombre.toLowerCase();
        if (lower.contains("escritorio") || lower.contains("e -")) return "Escritorio";
        if (lower.contains("sala") || lower.contains("s -")) return "Sala";
        if (lower.contains("área") || lower.contains("a -")) return "Área Común";
        if (lower.contains("libre") || lower.contains("l -")) return "Espacio Libre";
        return "Espacio General";
    }
    
    private double calculateSpacePrice(String tipo, double horas) {
        double basePrice = switch (tipo) {
            case "Escritorio" -> 3000;
            case "Sala" -> 8000;
            case "Área Común" -> 5000;
            case "Espacio Libre" -> 2000;
            default -> 4000;
        };
        return basePrice * horas;
    }
    
    private void updateInvoiceContent() {
        // Este método se llamaría después de setReservationData para actualizar el contenido
        Platform.runLater(() -> {
            detailsContainer.getChildren().clear();
            createInvoiceLayout();
        });
    }
    
 

    
   
    @FXML
  private void handleClose() {
    FadeTransition fade = new FadeTransition(Duration.millis(300), mainContainer);
    fade.setToValue(0);
    fade.setOnFinished(e -> {
        FlowController.getInstance().goView("NewReservation");
    });
    fade.play();
}
    
    private void playSuccessSound() {
        try {
            AudioClip sound = new AudioClip(getClass().getResource("/cr/ac/una/proyecto1progra2/resources/intro-sound-bell-269297-_1_.wav").toExternalForm());
            sound.play();
        } catch (Exception e) {
            System.out.println("Error al reproducir sonido: " + e.getMessage());
        }
    }
    
    @Override
    public void initialize() {
        // Implementación requerida por la clase Controller base
    }
}