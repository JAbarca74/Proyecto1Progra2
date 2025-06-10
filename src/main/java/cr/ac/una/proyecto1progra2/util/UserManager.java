package cr.ac.una.proyecto1progra2.util;

import cr.ac.una.proyecto1progra2.DTO.UsuariosDto;

public class UserManager {

    private static UsuariosDto currentUser;
     private static String discountCode;

    
    public static boolean hasDiscountCode() {
        return discountCode != null;
    }

    
    public static void assignDiscountCode(String code) {
        discountCode = code;
    }

    
    public static String getDiscountCode() {
        return discountCode;
    }

   
    public static UsuariosDto getCurrentUser() {
        return currentUser;
    }

    
    
      public static void setCurrentUser(UsuariosDto user) {
        currentUser = user;
    }
    public static void clearUser() {
        currentUser = null;
    }

    
    public static boolean isUserLoggedIn() {
        return currentUser != null;
    }

    
    public static boolean isAdmin() {
        return currentUser != null &&
               currentUser.getRolId() != null &&
               currentUser.getRolId().equals(Long.valueOf(1));
    }

    
    public static boolean isRegularUser() {
        return currentUser != null &&
               currentUser.getRolId() != null &&
               currentUser.getRolId().equals(Long.valueOf(2));
    }
}
