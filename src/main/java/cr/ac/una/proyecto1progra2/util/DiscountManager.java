package cr.ac.una.proyecto1progra2.util;

import java.security.SecureRandom;

public class DiscountManager {
    private static final String CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom rnd = new SecureRandom();

    
    public static String generateCode(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARSET.charAt(rnd.nextInt(CHARSET.length())));
        }
        return sb.toString();
    }
}
