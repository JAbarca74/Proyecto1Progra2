// src/main/java/cr/ac/una/proyecto1progra2/App.java
package cr.ac.una.proyecto1progra2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import cr.ac.una.proyecto1progra2.service.UserService;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        // Paso 6: cerrar el EntityManagerFactory de JPA
        UserService.closeDB();
        super.stop();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
            App.class.getResource("view/" + fxml + ".fxml")
        );
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
