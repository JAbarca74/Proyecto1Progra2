package cr.ac.una.proyecto1progra2.service;

import cr.ac.una.proyecto1progra2.model.CoworkingSpaces;
import cr.ac.una.proyecto1progra2.DTO.CoworkingSpacesDto;
import cr.ac.una.proyecto1progra2.util.EntityManagerHelper;
import cr.ac.una.proyecto1progra2.util.Respuesta;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CoworkingSpacesService {

    private final EntityManager em = EntityManagerHelper.getInstance().getManager();
    private EntityTransaction et;

    public Respuesta listarCoworkingSpaces() {
        try {
            TypedQuery<CoworkingSpaces> qry = em.createNamedQuery("CoworkingSpaces.findAll", CoworkingSpaces.class);
            List<CoworkingSpaces> list = qry.getResultList();
            List<CoworkingSpacesDto> dto = new ArrayList<>();
            for (CoworkingSpaces c : list) {
                dto.add(new CoworkingSpacesDto(c));
            }
            return new Respuesta(true, "", "", "Coworkings", dto);
        } catch (Exception ex) {
            Logger.getLogger(CoworkingSpacesService.class.getName()).log(Level.SEVERE, "Error listando coworkings.", ex);
            return new Respuesta(false, "Error listando coworkings.", ex.getMessage());
        }
    }

    public Respuesta getCoworkingSpace(Long id) {
        try {
            CoworkingSpaces e = em.find(CoworkingSpaces.class, id);
            if (e == null) {
                return new Respuesta(false, "No existe el coworking con ese id.", "");
            }
            return new Respuesta(true, "", "", "Coworking", new CoworkingSpacesDto(e));
        } catch (Exception ex) {
            Logger.getLogger(CoworkingSpacesService.class.getName()).log(Level.SEVERE, "Error obteniendo coworking [" + id + "]", ex);
            return new Respuesta(false, "Error obteniendo coworking.", ex.getMessage());
        }
    }

    public Respuesta guardarCoworkingSpace(CoworkingSpacesDto dto) {
        try {
            et = em.getTransaction();
            et.begin();
            CoworkingSpaces e;
            if (dto.getId() != null && dto.getId() > 0) {
                e = em.find(CoworkingSpaces.class, dto.getId());
                if (e == null) {
                    et.rollback();
                    return new Respuesta(false, "No se encontró el coworking a modificar.", "");
                }
                e.actualizar(dto);
                e = em.merge(e);
            } else {
                e = new CoworkingSpaces(dto);
                em.persist(e);
            }
            et.commit();
            return new Respuesta(true, "", "", "Coworking", new CoworkingSpacesDto(e));
        } catch (Exception ex) {
            if (et.isActive()) et.rollback();
            Logger.getLogger(CoworkingSpacesService.class.getName()).log(Level.SEVERE, "Error guardando coworking.", ex);
            return new Respuesta(false, "Error guardando coworking.", ex.getMessage());
        }
    }

    public Respuesta eliminarCoworkingSpace(Long id) {
        try {
            et = em.getTransaction();
            et.begin();
            CoworkingSpaces e = em.find(CoworkingSpaces.class, id);
            if (e == null) {
                et.rollback();
                return new Respuesta(false, "No se encontró el coworking a eliminar.", "");
            }
            em.remove(e);
            et.commit();
            return new Respuesta(true, "", "");
        } catch (Exception ex) {
            if (et.isActive()) et.rollback();
            Logger.getLogger(CoworkingSpacesService.class.getName()).log(Level.SEVERE, "Error eliminando coworking.", ex);
            return new Respuesta(false, "Error eliminando coworking.", ex.getMessage());
        }
    }
}
