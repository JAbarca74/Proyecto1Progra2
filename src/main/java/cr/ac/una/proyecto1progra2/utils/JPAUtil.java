package cr.ac.una.proyecto1progra2.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {
    private static final EntityManagerFactory emf =
        Persistence.createEntityManagerFactory(
          "cr.ac.una_Proyecto1Progra2_jar_1.0-SNAPSHOTPU"
        );

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void close() {
        emf.close();
    }
}
