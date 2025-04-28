package cr.ac.una.proyecto1progra2.service;

import cr.ac.una.proyecto1progra2.model.CoworkingSpaces;
import cr.ac.una.proyecto1progra2.model.CoworkingSpacesDto;
import cr.ac.una.proyecto1progra2.util.EntityManagerHelper;
import cr.ac.una.proyecto1progra2.util.Respuesta;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class CoworkingSpacesService {

    EntityManager em = EntityManagerHelper.getInstance().getManager();
    private EntityTransaction et;

    public Respuesta getCoworkingSpace(Long id) {
        try {
            Query qry = em.createNamedQuery("CoworkingSpaces.findById", CoworkingSpaces.class);
            qry.setParameter("id", id);
            CoworkingSpacesDto coworkingSpacesDto = new CoworkingSpacesDto((CoworkingSpaces) qry.getSingleResult());
            return new Respuesta(true, "", "", "CoworkingSpace", coworkingSpacesDto);
        } catch (NoResultException ex) {
            return new Respuesta(false, "No existe el coworking space con el id ingresado.", "getCoworkingSpace NoResultException");
        } catch (Exception ex) {
            Logger.getLogger(CoworkingSpacesService.class.getName()).log(Level.SEVERE, "Error obteniendo el coworking space [" + id + "]", ex);
            return new Respuesta(false, "Error obteniendo el coworking space.", "getCoworkingSpace " + ex.getMessage());
        }
    }

    public Respuesta guardarCoworkingSpace(CoworkingSpacesDto coworkingSpacesDto) {
        try {
            et = em.getTransaction();
            et.begin();
            CoworkingSpaces coworkingSpace;
            if (coworkingSpacesDto.getId() != null && coworkingSpacesDto.getId() > 0) {
                coworkingSpace = em.find(CoworkingSpaces.class, coworkingSpacesDto.getId());
                if (coworkingSpace == null) {
                    et.rollback();
                    return new Respuesta(false, "No se encontró el coworking space a modificar.", "guardarCoworkingSpace NoResultException");
                }
                coworkingSpace.actualizar(coworkingSpacesDto);
                coworkingSpace = em.merge(coworkingSpace);
            } else {
                coworkingSpace = new CoworkingSpaces(coworkingSpacesDto);
                em.persist(coworkingSpace);
            }
            et.commit();
            return new Respuesta(true, "", "", "CoworkingSpace", new CoworkingSpacesDto(coworkingSpace));
        } catch (Exception ex) {
            et.rollback();
            Logger.getLogger(CoworkingSpacesService.class.getName()).log(Level.SEVERE, "Ocurrió un error al guardar el coworking space.", ex);
            return new Respuesta(false, "Ocurrió un error al guardar el coworking space.", "guardarCoworkingSpace " + ex.getMessage());
        }
    }

    public Respuesta eliminarCoworkingSpace(Long id) {
        try {
            et = em.getTransaction();
            et.begin();
            CoworkingSpaces coworkingSpace;
            if (id != null && id > 0) {
                coworkingSpace = em.find(CoworkingSpaces.class, id);
                if (coworkingSpace == null) {
                    et.rollback();
                    return new Respuesta(false, "No se encontró el coworking space a eliminar.", "eliminarCoworkingSpace NoResultException");
                }
                em.remove(coworkingSpace);
            } else {
                et.rollback();
                return new Respuesta(false, "Debe cargar el coworking space a eliminar.", "eliminarCoworkingSpace NoResultException");
            }
            et.commit();
            return new Respuesta(true, "", "");
        } catch (Exception ex) {
            et.rollback();
            if (ex.getCause() != null && ex.getCause().getCause() instanceof SQLIntegrityConstraintViolationException) {
                return new Respuesta(false, "No se puede eliminar el coworking space porque tiene relaciones con otros registros.", "eliminarCoworkingSpace " + ex.getMessage());
            }
            Logger.getLogger(CoworkingSpacesService.class.getName()).log(Level.SEVERE, "Error eliminando el coworking space.", ex);
            return new Respuesta(false, "Error eliminando el coworking space.", "eliminarCoworkingSpace " + ex.getMessage());
        }
    }

    public Respuesta listarCoworkingSpaces() {
        try {
            Query qry = em.createNamedQuery("CoworkingSpaces.findAll", CoworkingSpaces.class);
            List<CoworkingSpaces> coworkingSpaces = qry.getResultList();
            List<CoworkingSpacesDto> coworkingSpacesDto = new ArrayList<>();
            for (CoworkingSpaces coworking : coworkingSpaces) {
                coworkingSpacesDto.add(new CoworkingSpacesDto(coworking));
            }
            return new Respuesta(true, "", "", "CoworkingSpaces", coworkingSpacesDto);
        } catch (Exception ex) {
            Logger.getLogger(CoworkingSpacesService.class.getName()).log(Level.SEVERE, "Error listando coworking spaces.", ex);
            return new Respuesta(false, "Error listando coworking spaces.", "listarCoworkingSpaces " + ex.getMessage());
        }
    }
}
