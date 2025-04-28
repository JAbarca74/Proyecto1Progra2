package cr.ac.una.proyecto1progra2;

import cr.ac.una.proyecto1progra2.util.FlowController;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FlowController.getInstance().initializeFlow(primaryStage, null);
        FlowController.getInstance().goMain();
    }

    public static void main(String[] args) {
        launch();
    }
}
