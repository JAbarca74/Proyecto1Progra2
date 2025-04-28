package cr.ac.una.proyecto1progra2.service;

import cr.ac.una.proyecto1progra2.model.SpaceTypes;
import cr.ac.una.proyecto1progra2.model.SpaceTypesDto;
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

public class SpaceTypesService {

    EntityManager em = EntityManagerHelper.getInstance().getManager();
    private EntityTransaction et;

    public Respuesta getSpaceType(Long id) {
        try {
            Query qry = em.createNamedQuery("SpaceTypes.findById", SpaceTypes.class);
            qry.setParameter("id", id);
            SpaceTypesDto spaceTypeDto = new SpaceTypesDto((SpaceTypes) qry.getSingleResult());
            return new Respuesta(true, "", "", "SpaceType", spaceTypeDto);
        } catch (NoResultException ex) {
            return new Respuesta(false, "No existe el tipo de espacio con el id ingresado.", "getSpaceType NoResultException");
        } catch (Exception ex) {
            Logger.getLogger(SpaceTypesService.class.getName()).log(Level.SEVERE, "Error obteniendo el tipo de espacio [" + id + "]", ex);
            return new Respuesta(false, "Error obteniendo el tipo de espacio.", "getSpaceType " + ex.getMessage());
        }
    }

    public Respuesta guardarSpaceType(SpaceTypesDto spaceTypeDto) {
        try {
            et = em.getTransaction();
            et.begin();
            SpaceTypes spaceType;
            if (spaceTypeDto.getId() != null && spaceTypeDto.getId() > 0) {
                spaceType = em.find(SpaceTypes.class, spaceTypeDto.getId());
                if (spaceType == null) {
                    et.rollback();
                    return new Respuesta(false, "No se encontró el tipo de espacio a modificar.", "guardarSpaceType NoResultException");
                }
                spaceType.actualizar(spaceTypeDto);
                spaceType = em.merge(spaceType);
            } else {
                spaceType = new SpaceTypes(spaceTypeDto);
                em.persist(spaceType);
            }
            et.commit();
            return new Respuesta(true, "", "", "SpaceType", new SpaceTypesDto(spaceType));
        } catch (Exception ex) {
            et.rollback();
            Logger.getLogger(SpaceTypesService.class.getName()).log(Level.SEVERE, "Ocurrió un error al guardar el tipo de espacio.", ex);
            return new Respuesta(false, "Ocurrió un error al guardar el tipo de espacio.", "guardarSpaceType " + ex.getMessage());
        }
    }

    public Respuesta eliminarSpaceType(Long id) {
        try {
            et = em.getTransaction();
            et.begin();
            SpaceTypes spaceType;
            if (id != null && id > 0) {
                spaceType = em.find(SpaceTypes.class, id);
                if (spaceType == null) {
                    et.rollback();
                    return new Respuesta(false, "No se encontró el tipo de espacio a eliminar.", "eliminarSpaceType NoResultException");
                }
                em.remove(spaceType);
            } else {
                et.rollback();
                return new Respuesta(false, "Debe cargar el tipo de espacio a eliminar.", "eliminarSpaceType NoResultException");
            }
            et.commit();
            return new Respuesta(true, "", "");
        } catch (Exception ex) {
            et.rollback();
            if (ex.getCause() != null && ex.getCause().getCause() instanceof SQLIntegrityConstraintViolationException) {
                return new Respuesta(false, "No se puede eliminar el tipo de espacio porque tiene relaciones con otros registros.", "eliminarSpaceType " + ex.getMessage());
            }
            Logger.getLogger(SpaceTypesService.class.getName()).log(Level.SEVERE, "Error eliminando el tipo de espacio.", ex);
            return new Respuesta(false, "Error eliminando el tipo de espacio.", "eliminarSpaceType " + ex.getMessage());
        }
    }

    public Respuesta listarSpaceTypes() {
        try {
            Query qry = em.createNamedQuery("SpaceTypes.findAll", SpaceTypes.class);
            List<SpaceTypes> spaceTypes = qry.getResultList();
            List<SpaceTypesDto> spaceTypesDto = new ArrayList<>();
            for (SpaceTypes type : spaceTypes) {
                spaceTypesDto.add(new SpaceTypesDto(type));
            }
            return new Respuesta(true, "", "", "SpaceTypes", spaceTypesDto);
        } catch (Exception ex) {
            Logger.getLogger(SpaceTypesService.class.getName()).log(Level.SEVERE, "Error listando tipos de espacio.", ex);
            return new Respuesta(false, "Error listando tipos de espacio.", "listarSpaceTypes " + ex.getMessage());
        }
    }
}
