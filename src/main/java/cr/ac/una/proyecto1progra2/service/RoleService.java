package cr.ac.una.proyecto1progra2.service;

import cr.ac.una.proyecto1progra2.model.Roles;
import cr.ac.una.proyecto1progra2.utils.JPAUtil;
import javax.persistence.EntityManager;
import java.util.List;

public class RoleService {
    public List<Roles> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createNamedQuery("Roles.findAll", Roles.class)
                     .getResultList();
        } finally {
            em.close();
        }
    }
}
