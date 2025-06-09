package cr.ac.una.proyecto1progra2.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerHelper {

    private static EntityManagerHelper instance;
    private EntityManagerFactory emf;
    private EntityManager em;

    private EntityManagerHelper() {
        emf = Persistence.createEntityManagerFactory("ProyectoPU");
        em = emf.createEntityManager();
    }

    public static EntityManagerHelper getInstance() {
        if (instance == null) {
            instance = new EntityManagerHelper();
        }
        return instance;
    }

    public EntityManager getManager() {
        return em;
    }
}
