package cr.ac.una.proyecto1progra2.util;

import cr.ac.una.proyecto1progra2.DTO.UsuariosDto;

public class UserManager {

    private static UsuariosDto currentUser;
     private static String discountCode;

    /** ¿Ya se le asignó un código en esta sesión? */
    public static boolean hasDiscountCode() {
        return discountCode != null;
    }

    /** Guarda el código asignado */
    public static void assignDiscountCode(String code) {
        discountCode = code;
    }

    /** Obtener el código actual (opcional) */
    public static String getDiscountCode() {
        return discountCode;
    }

    /**
     * Retorna el usuario actualmente autenticado.
     */
    public static UsuariosDto getCurrentUser() {
        return currentUser;
    }

    /**
     * Limpia al usuario actual (por ejemplo, al cerrar sesión).
     */
    
    
      public static void setCurrentUser(UsuariosDto user) {
        currentUser = user;
    }
    public static void clearUser() {
        currentUser = null;
    }

    /**
     * Retorna true si hay un usuario logueado.
     */
    public static boolean isUserLoggedIn() {
        return currentUser != null;
    }

    /**
     * Retorna true si el usuario tiene rol ADMIN.
     * Puedes cambiar el valor 1 según el ID del rol en tu base.
     */
    public static boolean isAdmin() {
        return currentUser != null &&
               currentUser.getRolId() != null &&
               currentUser.getRolId().equals(Long.valueOf(1));
    }

    /**
     * Retorna true si el usuario tiene rol normal (USER).
     * Puedes cambiar el valor 2 según el ID del rol en tu base.
     */
    public static boolean isRegularUser() {
        return currentUser != null &&
               currentUser.getRolId() != null &&
               currentUser.getRolId().equals(Long.valueOf(2));
    }
}
