package cr.ac.una.proyecto1progra2.controller;

import cr.ac.una.proyecto1progra2.util.DiscountManager;
import cr.ac.una.proyecto1progra2.util.UserManager;
import cr.ac.una.proyecto1progra2.util.MusicManager;
import cr.ac.una.proyecto1progra2.util.Sound;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;

public class MiniGameController implements Initializable {

    @FXML
    private AnchorPane root;
    @FXML
    private Label scoreLabel;
    @FXML
    private Button restartButton;
    @FXML
    private Button closeButton;
    @FXML
    private VBox gameOverContainer;
    @FXML
    private Label gameOverLabel;

    private AnchorPane gameArea;
    private final int UNIT_SIZE = 20;
    private final int WIDTH = 600;
    private final int HEIGHT = 400;

    private List<Rectangle> snake = new ArrayList<>();
    private Rectangle food;
    private Direction direction = Direction.RIGHT;
    private boolean running = false;
    private int score = 0;
    private AnimationTimer gameLoop;
    private long lastUpdate = 0;
    private Stage stage;
    private Random random = new Random();

    private enum Direction {
        UP, DOWN, LEFT, RIGHT
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MusicManager.pauseBackgroundMusic(); 
        MusicManager.playEffect("gaming.mp3",true);

        gameArea = new AnchorPane();
        gameArea.setPrefSize(WIDTH, HEIGHT);
        gameArea.getStyleClass().addAll("snake-game-area", "snake-grid-overlay");
        gameArea.setLayoutX(50);
        gameArea.setLayoutY(220);
        root.getChildren().add(gameArea);

        root.setFocusTraversable(true);
        root.setOnKeyPressed(this::handleKeyPress);
        startGame();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
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
                        checkFood();
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
        double x = head.getLayoutX();
        double y = head.getLayoutY();

        switch (direction) {
            case UP: y -= UNIT_SIZE; break;
            case DOWN: y += UNIT_SIZE; break;
            case LEFT: x -= UNIT_SIZE; break;
            case RIGHT: x += UNIT_SIZE; break;
        }

        Rectangle newHead = createRectangle(x, y, "snake-body");
        gameArea.getChildren().add(newHead);
        snake.add(0, newHead);

        head.getStyleClass().remove("snake-body");
        head.getStyleClass().add("snake-body");

        if (food != null && newHead.getBoundsInParent().intersects(food.getBoundsInParent())) {
            gameArea.getChildren().remove(food);
            Sound.playEatPoint();
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
        double x = head.getLayoutX();
        double y = head.getLayoutY();

        if (x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT ||
                snake.stream().skip(1).anyMatch(part -> part.getLayoutX() == x && part.getLayoutY() == y)) {
            endGame();
            Sound.playLose();
        }
    }

    private void checkFood() {
        if (food == null) return;
        Rectangle head = snake.get(0);
        if (head.getBoundsInParent().intersects(food.getBoundsInParent())) {

            }
        }

    private void spawnFood() {
    int cols = WIDTH / UNIT_SIZE;
    int rows = HEIGHT / UNIT_SIZE;
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
        switch (event.getCode()) {
            case UP: if (direction != Direction.DOWN) direction = Direction.UP; break;
            case DOWN: if (direction != Direction.UP) direction = Direction.DOWN; break;
            case LEFT: if (direction != Direction.RIGHT) direction = Direction.LEFT; break;
            case RIGHT: if (direction != Direction.LEFT) direction = Direction.RIGHT; break;
        }
    }
private void endGame() {
    // 1) Detén el juego
    running = false;
    if (gameLoop != null) gameLoop.stop();

    // 2) Muestra tu panel Game Over
    gameOverContainer.setVisible(true);
    gameOverLabel.setVisible(true);
    Label finalScore = (Label) gameOverContainer.getChildren().get(2);
    finalScore.setText("PUNTOS: " + score);

    // 3) Si toca el descuento
    if (score >= 100 && !UserManager.hasDiscountCode()) {
        String code = DiscountManager.generateCode(12);
        UserManager.assignDiscountCode(code);

        // 4) Programa el Alert para que se muestre DESPUÉS del frame actual
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("¡Has ganado un descuento!");
            alert.setHeaderText("Felicidades: llegaste a " + score + " puntos");
            // construimos un VBox para mezclar texto normal + negrita
            Label line1 = new Label("Por ser un gran jugador, obtienes un 30% de descuento.");
            Label codeLabel = new Label(code);
            codeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #e76f51;");
            Label line3 = new Label("Este código se canjeará UNA sola vez en el local al momento de cancelar.");
            VBox content = new VBox(10, line1, codeLabel, line3);
            content.setPadding(new Insets(10));
            alert.getDialogPane().setContent(content);

            alert.showAndWait();
        });
    }
}



    @FXML
    private void onActionReiniciar() {
        startGame();
        root.requestFocus();
    }

    @FXML
    private void onActionCerrarMiniJuego() {
        if (stage == null && closeButton != null) {
            stage = (Stage) closeButton.getScene().getWindow();
        }
        if (stage != null) {
             MusicManager.stopEffect();  
        MusicManager.resumeBackgroundMusic();
            stage.close();
        }
    }
}