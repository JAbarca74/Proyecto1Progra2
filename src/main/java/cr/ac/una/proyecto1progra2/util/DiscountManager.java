package cr.ac.una.proyecto1progra2.util;

import java.security.SecureRandom;

public class DiscountManager {
    private static final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom rnd = new SecureRandom();

    /** Genera un código aleatorio alfanumérico de la longitud dada */
    public static String generateCode(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARS.charAt(rnd.nextInt(CHARS.length())));
        }
        return sb.toString();
    }
}
