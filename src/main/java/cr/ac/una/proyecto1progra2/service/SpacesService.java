package cr.ac.una.proyecto1progra2.service;

import cr.ac.una.proyecto1progra2.model.Spaces;
import cr.ac.una.proyecto1progra2.DTO.SpacesDto;
import cr.ac.una.proyecto1progra2.model.CoworkingSpaces;
import cr.ac.una.proyecto1progra2.model.Reservations;
import cr.ac.una.proyecto1progra2.util.EntityManagerHelper;
import static cr.ac.una.proyecto1progra2.util.JPAUtil.getEntityManager;
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
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    try {
        tx.begin();
        Spaces e;
        boolean nuevo = false;

        if (dto.getId() != null && dto.getId() > 0) {
            e = em.find(Spaces.class, dto.getId());
            if (e == null) {
                tx.rollback();
                return new Respuesta(false, "No encontrado.", "");
            }
            e.actualizar(dto);
            e = em.merge(e);
        } else {
            e = new Spaces(dto);
            TypedQuery<Spaces> q = em.createNamedQuery("Spaces.findByName", Spaces.class);
            q.setParameter("name", dto.getNombre().toUpperCase());
            if (!q.getResultList().isEmpty()) {
                tx.rollback();
                return new Respuesta(false, "Ya existe un espacio con ese nombre", "");
            }
            em.persist(e);
            nuevo = true;
        }

        tx.commit();

        if (nuevo) {
            crearCoworkingSpaceSiNoExiste(e);
        }

        return new Respuesta(true, "", "", "Space", new SpacesDto(e));
    } catch (Exception ex) {
        if (tx.isActive()) tx.rollback();
        Logger.getLogger(SpacesService.class.getName()).log(Level.SEVERE, null, ex);
        return new Respuesta(false, "Error guardando espacio.", ex.getMessage());
    } finally {
        em.close();
    }
}
private void crearCoworkingSpaceSiNoExiste(Spaces espacio) {
    EntityManager em = emf.createEntityManager();
    try {
        em.getTransaction().begin();

        TypedQuery<CoworkingSpaces> query = em.createQuery(
            "SELECT c FROM CoworkingSpaces c WHERE c.spaceId.id = :spaceId", CoworkingSpaces.class);
        query.setParameter("spaceId", espacio.getId());
        List<CoworkingSpaces> resultado = query.getResultList();

        if (resultado.isEmpty()) {
            CoworkingSpaces nuevo = new CoworkingSpaces();
            nuevo.setId(espacio.getId()); // mismo ID que Space
            nuevo.setName("Espacio " + espacio.getId());
            nuevo.setCapacity(1);
            nuevo.setVersion(1L);
            nuevo.setSpaceId(espacio);

            // Establecer TYPE_ID por defecto (puede ajustarse si tenés lógica distinta)
            nuevo.setTypeId(em.find(cr.ac.una.proyecto1progra2.model.SpaceTypes.class, 1L));

            em.persist(nuevo);
        }

        em.getTransaction().commit();
    } catch (Exception ex) {
        ex.printStackTrace();
        if (em.getTransaction().isActive()) em.getTransaction().rollback();
    } finally {
        em.close();
    }
}
   public Respuesta eliminarSpace(Long id) {
    EntityManager em = getEntityManager();
    try {
        em.getTransaction().begin();

        Spaces espacio = em.find(Spaces.class, id);
        if (espacio == null)
            return new Respuesta(false, "No se encontró el espacio", "eliminarSpace NoResult");

        // Paso 1: Obtener coworking asociado
        CoworkingSpaces coworking = em.createQuery(
            "SELECT c FROM CoworkingSpaces c WHERE c.spaceId.id = :id", CoworkingSpaces.class)
            .setParameter("id", id)
            .getSingleResult();

        // Paso 2: Eliminar reservas asociadas
        List<Reservations> reservas = em.createQuery(
            "SELECT r FROM Reservations r WHERE r.coworkingSpaceId.id = :id", Reservations.class)
            .setParameter("id", coworking.getId())
            .getResultList();

        for (Reservations r : reservas) {
            em.remove(r);
        }

        // Paso 3: Eliminar coworking space
        em.remove(coworking);

        // Paso 4: Eliminar space
        em.remove(espacio);

        em.getTransaction().commit();
  return new Respuesta(true, "Espacio eliminado exitosamente.");
    } catch (NoResultException ex) {
        return new Respuesta(false, "Coworking space no encontrado para este espacio.", "eliminarSpace NoResult");
    } catch (Exception ex) {
        em.getTransaction().rollback();
        return new Respuesta(false, "Error al eliminar espacio: " + ex.getMessage(), "eliminarSpace " + ex.getMessage());
    } finally {
        em.close();
    }
}

    public List<SpaceVisual> obtenerEspaciosConPosicion() {
    EntityManager em = emf.createEntityManager();
    List<SpaceVisual> listaVisual = new ArrayList<>();
    try {
        List<Spaces> espacios = em.createQuery("SELECT s FROM Spaces s", Spaces.class).getResultList();
        for (Spaces space : espacios) {
            SpacesDto dto = new SpacesDto(space);
            // 👇 Relación directa desde Spaces al CoworkingSpaces
            dto.setCoworkingSpace(space.getCoworkingSpace());

            int fila = dto.getRow();
            int columna = dto.getColumn();
            int rowSpan = dto.getRowSpan() != null && dto.getRowSpan() > 0 ? dto.getRowSpan() : 1;
            int colSpan = dto.getColSpan() != null && dto.getColSpan() > 0 ? dto.getColSpan() : 1;

            listaVisual.add(new SpaceVisual(dto, fila, columna, rowSpan, colSpan));
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        em.close();
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
            CoworkingSpaces coworking = em.createQuery(
                "SELECT c FROM CoworkingSpaces c WHERE UPPER(c.name) = :nombre", CoworkingSpaces.class)
                .setParameter("nombre", nombrePiso.toUpperCase())
                .getResultStream()
                .findFirst()
                .orElse(null);
            if (coworking == null) {
                coworking = new CoworkingSpaces();
                coworking.setName(nombrePiso);
                coworking.setCapacity(0);
                coworking.setVersion(1L);
                em.persist(coworking);
            }

            int incremento = 0;
            String tipo = tipoEspacio.toLowerCase();
            if (tipo.equals("sala")) {
                incremento = 4;
            } else if (tipo.equals("escritorio")) {
                incremento = 1;
            } else if (tipo.equals("area comun")) {
                incremento = 2;
            }

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
    
    
    
    
    public List<CoworkingSpaces> obtenerCoworkingSpacesPorSpaceIds(List<Long> spaceIds) {
    EntityManager em = emf.createEntityManager();
    List<CoworkingSpaces> lista = new ArrayList<>();
    try {
        if (spaceIds == null || spaceIds.isEmpty()) return lista;
        TypedQuery<CoworkingSpaces> query = em.createQuery(
            "SELECT c FROM CoworkingSpaces c WHERE c.spaceId.id IN :ids", CoworkingSpaces.class);
        query.setParameter("ids", spaceIds);
        lista = query.getResultList();
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        em.close();
    }
    return lista;
}

}
