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
    
   public static boolean validateRegistration(String username, String password, String confirmPassword) {
        
       if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("Error", "Por favor, complete todos los campos.");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Las contraseñas no coinciden.");
            return false;
        }

        if ("admin".equals(username) || "user".equals(username)) {
            showAlert("Error", "El nombre de usuario ya está en uso.");
            return false;
        }

        return true;
    }
   
    public static void showAlert(String title, String content) {
        
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
