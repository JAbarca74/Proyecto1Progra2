// src/main/java/cr/ac/una/proyecto1progra2/service/UserService.java
package cr.ac.una.proyecto1progra2.service;

import cr.ac.una.proyecto1progra2.model.Usuarios;
import javafx.scene.control.Alert;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 * Servicio de usuario que combina:
 *  - Validación en memoria (mock hard-code)
 *  - Validación real vía JPA/EclipseLink contra TB_USUARIOS
 *  - Lógica de registro con alertas
 */
public class UserService {

    // Cambia a false para usar solo la validación mock
    private static final boolean USE_DB = true;

    // Nombre exacto de tu persistence-unit en persistence.xml
    private static final String PU_NAME = "cr.ac.una_Proyecto1Progra2_jar_1.0-SNAPSHOTPU";

    // EntityManagerFactory compartido
    private static final EntityManagerFactory emf =
        Persistence.createEntityManagerFactory(PU_NAME);

    /** Hard-code: admin/admin */
    public static boolean validateAdimin(String username, String password) {
        return "admin".equals(username) && "admin".equals(password);
    }

    /** Hard-code: user/user */
    public static boolean validateUser(String username, String password) {
        return "user".equals(username) && "user".equals(password);
    }

    /**
     * Valida contra la base de datos con JPA.
     * @return 1 si admin, 2 si usuario, -1 si falla
     */
    public static int validateLoginDB(String username, String password) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Usuarios> q = em.createNamedQuery(
                "Usuarios.findByUsername", Usuarios.class
            );
            q.setParameter("username", username);
            Usuarios u = q.getSingleResult();
            if (u != null
                && u.getPassword().equals(password)
                && Character.valueOf('Y').equals(u.getIsActive())) {
                return u.getRoleId().intValue();
            }
        } catch (Exception ex) {
            // no hay resultado o error
        } finally {
            em.close();
        }
        return -1;
    }

    /** Cierra el EntityManagerFactory al terminar la app */
    public static void closeDB() {
        if (emf.isOpen()) {
            emf.close();
        }
    }

    /**
     * Lógica de registro (igual que antes):
     * valida campos no vacíos, contraseñas coincidentes, evita nombres reservados.
     */
    public static boolean validateRegistration(
            String username, String password, String confirmPassword) {

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

    /** Muestra un Alert de tipo ERROR */
    public static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
