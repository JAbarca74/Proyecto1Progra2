package cr.ac.una.proyecto1progra2.service;

import cr.ac.una.proyecto1progra2.model.Spaces;
import cr.ac.una.proyecto1progra2.model.SpacesDto;
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

public class SpacesService {

    EntityManager em = EntityManagerHelper.getInstance().getManager();
    private EntityTransaction et;

    public Respuesta getSpace(Long id) {
        try {
            Query qrySpace = em.createNamedQuery("Spaces.findById", Spaces.class);
            qrySpace.setParameter("id", id);
            SpacesDto spaceDto = new SpacesDto((Spaces) qrySpace.getSingleResult());
            return new Respuesta(true, "", "", "Space", spaceDto);
        } catch (NoResultException ex) {
            return new Respuesta(false, "No existe el espacio con el id ingresado.", "getSpace NoResultException");
        } catch (Exception ex) {
            Logger.getLogger(SpacesService.class.getName()).log(Level.SEVERE, "Error obteniendo el espacio [" + id + "]", ex);
            return new Respuesta(false, "Error obteniendo el espacio.", "getSpace " + ex.getMessage());
        }
    }

    public Respuesta guardarSpace(SpacesDto spaceDto) {
        try {
            et = em.getTransaction();
            et.begin();
            Spaces space;
            if (spaceDto.getId() != null && spaceDto.getId() > 0) {
                space = em.find(Spaces.class, spaceDto.getId());
                if (space == null) {
                    et.rollback();
                    return new Respuesta(false, "No se encontró el espacio a modificar.", "guardarSpace NoResultException");
                }
                space.actualizar(spaceDto);
                space = em.merge(space);
            } else {
                space = new Spaces(spaceDto);
                em.persist(space);
            }
            et.commit();
            return new Respuesta(true, "", "", "Space", new SpacesDto(space));
        } catch (Exception ex) {
            et.rollback();
            Logger.getLogger(SpacesService.class.getName()).log(Level.SEVERE, "Ocurrió un error al guardar el espacio.", ex);
            return new Respuesta(false, "Ocurrió un error al guardar el espacio.", "guardarSpace " + ex.getMessage());
        }
    }

    public Respuesta eliminarSpace(Long id) {
        try {
            et = em.getTransaction();
            et.begin();
            Spaces space;
            if (id != null && id > 0) {
                space = em.find(Spaces.class, id);
                if (space == null) {
                    et.rollback();
                    return new Respuesta(false, "No se encontró el espacio a eliminar.", "eliminarSpace NoResultException");
                }
                em.remove(space);
            } else {
                et.rollback();
                return new Respuesta(false, "Debe cargar el espacio a eliminar.", "eliminarSpace NoResultException");
            }
            et.commit();
            return new Respuesta(true, "", "");
        } catch (Exception ex) {
            et.rollback();
            if (ex.getCause() != null && ex.getCause().getCause() instanceof SQLIntegrityConstraintViolationException) {
                return new Respuesta(false, "No se puede eliminar el espacio porque tiene relaciones con otros registros.", "eliminarSpace " + ex.getMessage());
            }
            Logger.getLogger(SpacesService.class.getName()).log(Level.SEVERE, "Error eliminando el espacio.", ex);
            return new Respuesta(false, "Error eliminando el espacio.", "eliminarSpace " + ex.getMessage());
        }
    }

    public Respuesta listarSpaces() {
        try {
            Query qry = em.createNamedQuery("Spaces.findAll", Spaces.class);
            List<Spaces> spaces = qry.getResultList();
            List<SpacesDto> spacesDto = new ArrayList<>();
            for (Spaces space : spaces) {
                spacesDto.add(new SpacesDto(space));
            }
            return new Respuesta(true, "", "", "Spaces", spacesDto);
        } catch (Exception ex) {
            Logger.getLogger(SpacesService.class.getName()).log(Level.SEVERE, "Error listando espacios.", ex);
            return new Respuesta(false, "Error listando espacios.", "listarSpaces " + ex.getMessage());
        }
    }
}
