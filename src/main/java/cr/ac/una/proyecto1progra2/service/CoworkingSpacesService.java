package cr.ac.una.proyecto1progra2.service;

import cr.ac.una.proyecto1progra2.model.CoworkingSpaces;
import cr.ac.una.proyecto1progra2.util.JPAUtil;


import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class CoworkingSpacesService {

    public CoworkingSpaces buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(CoworkingSpaces.class, id);
        } finally {
            em.close();
        }
    }

    public List<CoworkingSpaces> listarTodo() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<CoworkingSpaces> query = em.createQuery("SELECT c FROM CoworkingSpaces c", CoworkingSpaces.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}  
