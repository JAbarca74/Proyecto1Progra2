package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.util.DiscountManager;
import cr.ac.una.proyecto1progra2.util.UserManager;
import cr.ac.una.proyecto1progra2.util.MusicManager;
import cr.ac.una.proyecto1progra2.util.Sound;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.PauseTransition;
import java.net.URL;
import java.util.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class MiniGameController extends Controller implements Initializable {

    @FXML private AnchorPane root;
    @FXML private Label scoreLabel;
    @FXML private Button restartButton;
    @FXML private Button closeButton;
    @FXML private VBox gameOverContainer;
    @FXML private Label gameOverLabel;
        @FXML private Label trophyIcon;
    @FXML private Label lightningIcon;
    @FXML private HBox consoleControl;


    private AnchorPane gameArea;
    private final int UNIT_SIZE = 20, WIDTH = 600, HEIGHT = 400;
    private List<Rectangle> snake = new ArrayList<>();
    private Rectangle food;
    private Direction direction = Direction.RIGHT;
    private boolean running;
    private int score;
    private AnimationTimer gameLoop;
    private long lastUpdate;

    private Random random = new Random();

    

    private enum Direction { UP, DOWN, LEFT, RIGHT }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       MusicManager.pauseBackgroundMusic();
    MusicManager.playEffect("gaming.mp3", true);
    gameArea = new AnchorPane();
    gameArea.setPrefSize(WIDTH, HEIGHT);
    gameArea.getStyleClass().addAll("snake-game-area", "snake-grid-overlay");
    gameArea.setLayoutX(50);
    gameArea.setLayoutY(220);
    root.getChildren().add(gameArea);
    root.setFocusTraversable(true);
    root.setOnKeyPressed(this::handleKeyPress);
    startGame();
    Platform.runLater(()-> root.requestFocus());
    }

    @Override
public void setStage(Stage stage) {
    super.setStage(stage);
    if (stage != null) {
        
        stage.addEventHandler(WindowEvent.WINDOW_SHOWN, e -> root.requestFocus());
        
        stage.addEventHandler(WindowEvent.WINDOW_HIDDEN, e -> MusicManager.stopEffect());
    }
}


    private void startGame() {
    snake.clear();
    gameArea.getChildren().clear();
    direction = Direction.RIGHT;
    running = true;
    score = 0;
    updateScore();

    gameOverContainer.setVisible(false);
    gameOverLabel.setVisible(false);

    Rectangle head = createRectangle(3 * UNIT_SIZE, 0, "snake-body");
    snake.add(head);
    gameArea.getChildren().add(head);
    for (int i = 1; i < 3; i++) {
        Rectangle body = createRectangle((3 - i) * UNIT_SIZE, 0, "snake-body");
        snake.add(body);
        gameArea.getChildren().add(body);
    }

    spawnFood();

    gameLoop = new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (now - lastUpdate > 150_000_000) {
                if (running) {
                    move();
                    checkCollision();
                }
                lastUpdate = now;
            }
        }
    };
    gameLoop.start();
}

    private Rectangle createRectangle(double x, double y, String styleClass) {
        Rectangle rect = new Rectangle(UNIT_SIZE, UNIT_SIZE);
        rect.setLayoutX(x);
        rect.setLayoutY(y);
        rect.getStyleClass().add(styleClass);
        return rect;
    }

    private void move() {
        Rectangle head = snake.get(0);
        double x = head.getLayoutX(), y = head.getLayoutY();
        switch (direction) {
            case UP:    y -= UNIT_SIZE; break;
            case DOWN:  y += UNIT_SIZE; break;
            case LEFT:  x -= UNIT_SIZE; break;
            case RIGHT: x += UNIT_SIZE; break;
        }

        Rectangle newHead = createRectangle(x, y, "snake-body");
        gameArea.getChildren().add(newHead);
        snake.add(0, newHead);

        if (food != null && newHead.getBoundsInParent().intersects(food.getBoundsInParent())) {
            gameArea.getChildren().remove(food);
            Sound.playEatPoint();

            lightningIcon.getStyleClass().add("lit");
            PauseTransition flash = new PauseTransition(Duration.millis(200));
            flash.setOnFinished(e -> lightningIcon.getStyleClass().remove("lit"));
            flash.play();

            spawnFood();
            score += 10;
            updateScore();
        } else {
            Rectangle tail = snake.remove(snake.size() - 1);
            gameArea.getChildren().remove(tail);
        }
    }

    private void checkCollision() {
        Rectangle head = snake.get(0);
        double x = head.getLayoutX(), y = head.getLayoutY();
        boolean selfHit = snake.stream()
                .skip(1)
                .anyMatch(p -> p.getLayoutX() == x && p.getLayoutY() == y);
        if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT || selfHit) {
            endGame();
            Sound.playLose();
        }
    }

    private void spawnFood() {
        int cols = WIDTH / UNIT_SIZE, rows = HEIGHT / UNIT_SIZE;
        int x, y;
        boolean valid;
        do {
            x = random.nextInt(cols) * UNIT_SIZE;
            y = random.nextInt(rows) * UNIT_SIZE;
            valid = true;
            for (Rectangle part : snake) {
                if (part.getLayoutX() == x && part.getLayoutY() == y) {
                    valid = false;
                    break;
                }
            }
        } while (!valid);

        food = createRectangle(x, y, "snake-food");
        gameArea.getChildren().add(food);
    }

    private void updateScore() {
        scoreLabel.setText("PUNTOS: " + score);
    }

    private void handleKeyPress(KeyEvent event) {
        if (!running && event.getCode() == KeyCode.SPACE) {
            startGame();
            return;
        }
        if (!running) return;

        consoleControl.getStyleClass().add("lit-control");
        PauseTransition t = new PauseTransition(Duration.millis(100));
        t.setOnFinished(e -> consoleControl.getStyleClass().remove("lit-control"));
        t.play();

        switch (event.getCode()) {
            case UP:    if (direction != Direction.DOWN)  direction = Direction.UP;    break;
            case DOWN:  if (direction != Direction.UP)    direction = Direction.DOWN;  break;
            case LEFT:  if (direction != Direction.RIGHT) direction = Direction.LEFT;  break;
            case RIGHT: if (direction != Direction.LEFT)  direction = Direction.RIGHT; break;
            default: break;
        }
    }

    private void endGame() {
        running = false;
        if (gameLoop != null) gameLoop.stop();

        gameOverContainer.setVisible(true);
        gameOverLabel.setVisible(true);
        Label finalScore = (Label) gameOverContainer.getChildren().get(2);
        finalScore.setText("PUNTOS: " + score);

        if (score >= 150) {
            trophyIcon.getStyleClass().add("lit");
        }

        int discount = score >= 150 ? 30 : (score >= 100 ? 15 : 0);
        if (discount > 0 && !UserManager.hasDiscountCode()) {
            String raw = DiscountManager.generateCode(12);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < raw.length(); i++) {
                if (i > 0 && i % 4 == 0) sb.append('-');
                sb.append(raw.charAt(i));
            }
            String code = sb.toString();
            UserManager.assignDiscountCode(code);

            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("¡Descuento " + discount + "%!");
                alert.setHeaderText("¡Felicidades! Alcanzaste " + score + " puntos");
                Label l1 = new Label("Obtienes un " + discount + "% de descuento.");
                Label c  = new Label(code);
                c.setStyle("-fx-font-weight:bold; -fx-font-size:16px; -fx-text-fill:#e76f51;");
                Label l3 = new Label("No lo pierdas. Canjéalo una sola vez al cancelar en el local.");
                VBox vb = new VBox(10, l1, c, l3);
                vb.setPadding(new Insets(10));
                alert.getDialogPane().setContent(vb);
                alert.showAndWait();
            });
        }
    }

    @FXML private void onActionReiniciar() {
        startGame();
        root.requestFocus();
    }
    @FXML private void onActionCerrarMiniJuego() { if (gameLoop != null) gameLoop.stop(); 
    MusicManager.stopEffect();                                  
          if (getStage() != null) {
            getStage().close();
        }
    
    }
    
    

    @Override
    public void initialize() {
        
    }

    
}
