package cr.ac.una.proyecto1progra2.service;

import cr.ac.una.proyecto1progra2.model.SpaceTypes;
import cr.ac.una.proyecto1progra2.DTO.SpaceTypesDto;
import cr.ac.una.proyecto1progra2.util.EntityManagerHelper;
import cr.ac.una.proyecto1progra2.util.Respuesta;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SpaceTypesService {

    private final EntityManager em = EntityManagerHelper.getInstance().getManager();
    private EntityTransaction tx;

    @SuppressWarnings("unchecked")
    public Respuesta listarTipos() {
        try {
            Query q = em.createNamedQuery("SpaceTypes.findAll", SpaceTypes.class);
            List<SpaceTypes> list = q.getResultList();
            List<SpaceTypesDto> dto = new ArrayList<>();
            for (SpaceTypes e : list) dto.add(new SpaceTypesDto(e));
            return new Respuesta(true, "", "", "Tipos", dto);
        } catch (Exception ex) {
            Logger.getLogger(SpaceTypesService.class.getName()).log(Level.SEVERE, null, ex);
            return new Respuesta(false, "Error listando tipos.", "listarTipos " + ex.getMessage());
        }
    }

    public Respuesta getTipo(Long id) {
        try {
            SpaceTypes e = em.find(SpaceTypes.class, id);
            if (e == null)
                return new Respuesta(false, "No existe tipo con ese id.", "getTipo NoResult");
            return new Respuesta(true, "", "", "Tipo", new SpaceTypesDto(e));
        } catch (Exception ex) {
            Logger.getLogger(SpaceTypesService.class.getName()).log(Level.SEVERE, null, ex);
            return new Respuesta(false, "Error obteniendo tipo.", "getTipo " + ex.getMessage());
        }
    }

    public Respuesta guardarTipo(SpaceTypesDto dto) {
        try {
            tx = em.getTransaction(); tx.begin();
            SpaceTypes e;
            if (dto.getId() != null && dto.getId() > 0) {
                e = em.find(SpaceTypes.class, dto.getId());
                if (e == null) {
                    tx.rollback();
                    return new Respuesta(false, "Tipo no encontrado.", "guardarTipo");
                }
                e.setTypeName(dto.getTypeName());
             
                e = em.merge(e);
            } else {
                e = new SpaceTypes(dto);
                em.persist(e);
            }
            tx.commit();
            return new Respuesta(true, "", "", "Tipo", new SpaceTypesDto(e));
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            Logger.getLogger(SpaceTypesService.class.getName()).log(Level.SEVERE, null, ex);
            return new Respuesta(false, "Error guardando tipo.", "guardarTipo " + ex.getMessage());
        }
    }

    public Respuesta eliminarTipo(Long id) {
        try {
            tx = em.getTransaction(); tx.begin();
            SpaceTypes e = em.find(SpaceTypes.class, id);
            if (e == null) {
                tx.rollback();
                return new Respuesta(false, "Tipo no encontrado.", "eliminarTipo");
            }
            em.remove(e);
            tx.commit();
            return new Respuesta(true, "", "");
        } catch (Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            Logger.getLogger(SpaceTypesService.class.getName()).log(Level.SEVERE, null, ex);
            return new Respuesta(false, "Error eliminando tipo.", "eliminarTipo " + ex.getMessage());
        }
    }
}
