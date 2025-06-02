package cr.ac.una.proyecto1progra2.service;

import cr.ac.una.proyecto1progra2.model.Spaces;
import cr.ac.una.proyecto1progra2.DTO.SpacesDto;
import cr.ac.una.proyecto1progra2.model.CoworkingSpaces;
import cr.ac.una.proyecto1progra2.util.EntityManagerHelper;
import cr.ac.una.proyecto1progra2.util.Respuesta;
import cr.ac.una.proyecto1progra2.util.SpaceVisual;

import javax.persistence.*;
import java.util.*;
import java.util.logging.*;

public class SpacesService {
    private final EntityManager em = EntityManagerHelper.getInstance().getManager();
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("ProyectoPU");
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
public void actualizarCapacidadCoworkingSpace(int piso, int nuevaCapacidad) {
    EntityManager em = emf.createEntityManager();
    try {
        em.getTransaction().begin();
        TypedQuery<CoworkingSpaces> query = em.createQuery(
            "SELECT c FROM CoworkingSpaces c WHERE UPPER(c.name) LIKE :nombre", CoworkingSpaces.class);
        query.setParameter("nombre", "%" + ("PISO " + piso).toUpperCase());
        List<CoworkingSpaces> resultados = query.getResultList();

        if (!resultados.isEmpty()) {
            CoworkingSpaces coworking = resultados.get(0);
            coworking.setCapacity(nuevaCapacidad);
            em.merge(coworking);
        }

        em.getTransaction().commit();
    } catch (Exception e) {
        e.printStackTrace();
        if (em.getTransaction().isActive()) em.getTransaction().rollback();
    } finally {
        em.close();
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
            et = em.getTransaction();
            et.begin();
            Spaces e;

            if (dto.getId() != null && dto.getId() > 0) {
                e = em.find(Spaces.class, dto.getId());
                if (e == null) {
                    et.rollback();
                    return new Respuesta(false, "No encontrado.", "");
                }
                e.actualizar(dto);
                e = em.merge(e);
            } else {
                e = new Spaces(dto);
                TypedQuery<Spaces> q = em.createNamedQuery("Spaces.findByName", Spaces.class);
                q.setParameter("name", dto.getNombre().toUpperCase());
                if (!q.getResultList().isEmpty()) {
                    et.rollback();
                    return new Respuesta(false, "Ya existe un espacio con ese nombre", "");
                }
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

    public void eliminarSpace(Long id) {
        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
            Spaces space = em.find(Spaces.class, id);
            if (space != null) {
                em.remove(space);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        }
    }

    public List<SpaceVisual> obtenerEspaciosConPosicion() {
        List<SpaceVisual> listaVisual = new ArrayList<>();
        Respuesta resp = listarSpaces();
        if (!resp.isSuccess()) return listaVisual;

        List<SpacesDto> espacios = (List<SpacesDto>) resp.getResultado("Spaces");

        for (SpacesDto dto : espacios) {
            int fila = dto.getRow();
            int columna = dto.getColumn();
            int rowSpan = dto.getRowSpan();
            int colSpan = dto.getColSpan();

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

            int borradosReservas = em.createQuery("DELETE FROM Reservations").executeUpdate();
            int borradosCoworking = em.createQuery("DELETE FROM CoworkingSpaces").executeUpdate();
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
public void incrementarCapacidadPorEspacio(int piso, String tipoEspacio) {
    EntityManager em = emf.createEntityManager();
    try {
        em.getTransaction().begin();

        String nombrePiso = "PISO " + piso;

        // Buscar el coworking space por nombre
        CoworkingSpaces coworking = em.createQuery(
            "SELECT c FROM CoworkingSpaces c WHERE UPPER(c.name) = :nombre", CoworkingSpaces.class)
            .setParameter("nombre", nombrePiso.toUpperCase())
            .getResultStream()
            .findFirst()
            .orElse(null);

        // Si no existe, se crea con capacidad inicial 0
        if (coworking == null) {
            coworking = new CoworkingSpaces();
            coworking.setName(nombrePiso);
            coworking.setCapacity(0);
            coworking.setVersion(1L);
            em.persist(coworking);
        }

        // Sumar según el tipo de espacio
        int incremento = switch (tipoEspacio.toLowerCase()) {
            case "sala" -> 4;
            case "escritorio" -> 1;
            case "area comun" -> 2;
            default -> 0; // espacios libres o no reconocidos
        };

        coworking.setCapacity(coworking.getCapacity() + incremento);
        em.merge(coworking);

        em.getTransaction().commit();
    } catch (Exception e) {
        e.printStackTrace();
        if (em.getTransaction().isActive()) em.getTransaction().rollback();
    } finally {
        em.close();
    }
}
    public Respuesta eliminarEspaciosPorPiso(int piso) {
        try {
            List<SpaceVisual> espacios = obtenerEspaciosConPosicion();
            String filtro = "P" + piso;

            for (SpaceVisual esp : espacios) {
                if (esp.getSpace().getNombre().contains(filtro)) {
                    eliminarSpace(esp.getSpace().getId());
                }
            }

            return new Respuesta(true, "Espacios del piso eliminados correctamente.", "");
        } catch (Exception e) {
            return new Respuesta(false, "Error eliminando espacios del piso.", e.getMessage());
        }
    }
}