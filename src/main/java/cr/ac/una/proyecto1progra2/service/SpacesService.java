package cr.ac.una.proyecto1progra2.service;

import cr.ac.una.proyecto1progra2.model.Spaces;
import cr.ac.una.proyecto1progra2.DTO.SpacesDto;
import cr.ac.una.proyecto1progra2.util.EntityManagerHelper;
import cr.ac.una.proyecto1progra2.util.Respuesta;
import cr.ac.una.proyecto1progra2.util.SpaceVisual;

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
    TypedQuery<Spaces> q = em.createNamedQuery("Spaces.findByName", Spaces.class);
q.setParameter("name", dto.getNombre().toUpperCase());
if (!q.getResultList().isEmpty()) {
    return new Respuesta(false, "Ya existe un espacio con ese nombre", "");
}// Si aquí el constructor asigna un ID manual, mal
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

    // Nuevo método que retorna una lista de SpaceVisual con posición y tamaño para UI
  public List<SpaceVisual> obtenerEspaciosConPosicion() {
    List<SpaceVisual> listaVisual = new ArrayList<>();
    Respuesta resp = listarSpaces();
    if (!resp.isSuccess()) return listaVisual;

    List<SpacesDto> espacios = (List<SpacesDto>) resp.getResultado("Spaces");

    for (SpacesDto dto : espacios) {
        int fila = dto.getRow();       // Ahora tomado del DTO
        int columna = dto.getColumn(); // Ahora tomado del DTO
        int rowSpan = dto.getRowSpan(); // Si tienes este dato, sino 1
        int colSpan = dto.getColSpan(); // Si tienes este dato, sino 1

        if (rowSpan <= 0) rowSpan = 1;
        if (colSpan <= 0) colSpan = 1;

        listaVisual.add(new SpaceVisual(dto, fila, columna, rowSpan, colSpan));
    }
    return listaVisual;
}
   
   public Respuesta eliminarTodosSpaces() {
    try {
        et = em.getTransaction();
        et.begin();

        // 1. Borrar primero las reservas (hijas de CoworkingSpaces)
        int borradosReservas = em.createQuery("DELETE FROM Reservations").executeUpdate();

        // 2. Luego borrar los coworkingSpaces
        int borradosCoworking = em.createQuery("DELETE FROM CoworkingSpaces").executeUpdate();

        // 3. Finalmente borrar los spaces
        int borradosSpaces = em.createQuery("DELETE FROM Spaces").executeUpdate();

        et.commit();
        return new Respuesta(true,
            "Se eliminaron " + borradosReservas + " reservas, " +
            borradosCoworking + " coworkings y " +
            borradosSpaces + " espacios.",
            "");
    } catch (Exception ex) {
        if (et.isActive()) et.rollback();
        Logger.getLogger(SpacesService.class.getName()).log(Level.SEVERE, null, ex);
        return new Respuesta(false, "Error eliminando espacios.", ex.getMessage());
    }
}

   // En SpacesService.java

}