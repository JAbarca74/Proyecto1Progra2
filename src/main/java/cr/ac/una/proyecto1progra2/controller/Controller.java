package cr.ac.una.proyecto1progra2.controller;

import javafx.scene.control.Control;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public abstract class Controller {

    protected Stage stage;
    private String accion;

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void sendTabEvent(KeyEvent event) {
        event.consume();
        KeyEvent tabEvent = new KeyEvent(
                KeyEvent.KEY_PRESSED, "", "", KeyCode.TAB,
                false, false, false, false
        );
        ((Control) event.getSource()).fireEvent(tabEvent);
    }

    public abstract void initialize();
}
