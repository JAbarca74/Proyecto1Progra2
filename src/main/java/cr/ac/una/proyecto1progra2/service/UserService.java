package cr.ac.una.proyecto1progra2.service;

import javafx.scene.control.Alert;

public class UserService {
    
    public static boolean validateAdimin(String username, String password) {
        if ("admin".equals(username) && "admin".equals(password)) {
            return true; 
        } else {
            return false;          
        }
    }
    
       
    public static boolean validateUser(String username, String password) {
        if ("user".equals(username) && "user".equals(password)) {
            return true; 
        } else {
            return false;          
        }
    }
   
    public static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
