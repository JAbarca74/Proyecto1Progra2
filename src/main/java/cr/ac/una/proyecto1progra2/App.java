package cr.ac.una.proyecto1progra2;

import cr.ac.una.proyecto1progra2.util.FlowController;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FlowController.getInstance().initializeFlow(stage, null);

        stage.setMinWidth(1000);
        stage.setMinHeight(700);

        FlowController.getInstance().goViewInWindow("primary");
    }

    public static void main(String[] args) {
        launch();
    }
}