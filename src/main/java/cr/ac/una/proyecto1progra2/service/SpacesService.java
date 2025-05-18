package cr.ac.una.proyecto1progra2.service;

import cr.ac.una.proyecto1progra2.model.Spaces;
import cr.ac.una.proyecto1progra2.model.SpacesDto;
import cr.ac.una.proyecto1progra2.util.EntityManagerHelper;
import cr.ac.una.proyecto1progra2.util.Respuesta;

import javax.persistence.*;
import java.util.*;
import java.util.logging.*;

public class SpacesService {
    private final EntityManager em = EntityManagerHelper.getInstance().getManager();
    private EntityTransaction et;

    public Respuesta listarSpaces() {
        try {
            TypedQuery<Spaces> q = em.createNamedQuery("Spaces.findAll", Spaces.class);
            List<Spaces> l = q.getResultList();
            List<SpacesDto> dto = new ArrayList<>();
            for (Spaces s : l) dto.add(new SpacesDto(s));
            return new Respuesta(true, "", "", "Spaces", dto);
        } catch (Exception ex) {
            Logger.getLogger(SpacesService.class.getName()).log(Level.SEVERE, null, ex);
            return new Respuesta(false, "Error listando espacios.", ex.getMessage());
        }
    }

    public Respuesta getSpace(Long id) {
        try {
            Spaces s = em.find(Spaces.class, id);
            if (s == null) return new Respuesta(false, "No existe espacio.", "");
            return new Respuesta(true, "", "", "Space", new SpacesDto(s));
        } catch (Exception ex) {
            Logger.getLogger(SpacesService.class.getName()).log(Level.SEVERE, null, ex);
            return new Respuesta(false, "Error obteniendo espacio.", ex.getMessage());
        }
    }

    public Respuesta guardarSpace(SpacesDto dto) {
        try {
            et = em.getTransaction(); et.begin();
            Spaces e;
            if (dto.getId() != null && dto.getId() > 0) {
                e = em.find(Spaces.class, dto.getId());
                if (e == null) { et.rollback(); return new Respuesta(false,"No encontrado.",""); }
                e.actualizar(dto);
                e = em.merge(e);
            } else {
                e = new Spaces(dto);
                em.persist(e);
            }
            et.commit();
            return new Respuesta(true, "", "", "Space", new SpacesDto(e));
        } catch (Exception ex) {
            if (et.isActive()) et.rollback();
            Logger.getLogger(SpacesService.class.getName()).log(Level.SEVERE, null, ex);
            return new Respuesta(false, "Error guardando espacio.", ex.getMessage());
        }
    }

    public Respuesta eliminarSpace(Long id) {
        try {
            et = em.getTransaction(); et.begin();
            Spaces e = em.find(Spaces.class, id);
            if (e == null) { et.rollback(); return new Respuesta(false,"No encontrado.",""); }
            em.remove(e);
            et.commit();
            return new Respuesta(true, "Eliminado.", "");
        } catch (Exception ex) {
            if (et.isActive()) et.rollback();
            Logger.getLogger(SpacesService.class.getName()).log(Level.SEVERE, null, ex);
            return new Respuesta(false, "Error eliminando espacio.", ex.getMessage());
        }
    }
}
