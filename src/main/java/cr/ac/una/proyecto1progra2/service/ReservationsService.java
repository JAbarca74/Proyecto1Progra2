package cr.ac.una.proyecto1progra2.service;

import cr.ac.una.proyecto1progra2.DTO.ReservationViewDto;
import cr.ac.una.proyecto1progra2.model.CoworkingSpaces;
import cr.ac.una.proyecto1progra2.model.Reservations;
import cr.ac.una.proyecto1progra2.model.Usuarios;
import cr.ac.una.proyecto1progra2.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationsService {

    
    public List<Reservations> buscarReservasTraslapadas(Long coworkingSpaceId,
                                                        LocalDate fecha,
                                                        LocalTime horaInicio,
                                                        LocalTime horaFin) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Reservations> q = em.createQuery(
                "SELECT r FROM Reservations r " +
                "WHERE r.coworkingSpaceId.id = :spaceId " +
                  "AND r.reservationDate = :fecha " +
                  "AND r.startTime < :horaFin " +
                  "AND r.endTime > :horaInicio",
                Reservations.class
            );
            q.setParameter("spaceId",   coworkingSpaceId);
            q.setParameter("fecha",     fecha);
            q.setParameter("horaInicio",horaInicio);
            q.setParameter("horaFin",   horaFin);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    
   public boolean guardarReserva(Long userId,
                              Long coworkingSpaceId,
                              LocalDate fecha,
                              LocalTime horaInicio,
                              LocalTime horaFin) {
    if (userId == null || coworkingSpaceId == null || fecha == null ||
        horaInicio == null || horaFin == null || !horaInicio.isBefore(horaFin)) {
        return false;
    }

    EntityManager em = JPAUtil.getEntityManager();
    try {
        
        List<Reservations> existentes = buscarReservasTraslapadas(coworkingSpaceId, fecha, horaInicio, horaFin);
        if (!existentes.isEmpty()) {
            return false; 
        }

        em.getTransaction().begin();
        Reservations r = new Reservations();
        r.setUserId(em.getReference(Usuarios.class, userId));
        r.setCoworkingSpaceId(em.getReference(CoworkingSpaces.class, coworkingSpaceId));
        r.setReservationDate(fecha);
        r.setStartTime(horaInicio);
        r.setEndTime(horaFin);
        em.persist(r);
        em.getTransaction().commit();
        return true;
    } catch (Exception ex) {
        if (em.getTransaction().isActive()) em.getTransaction().rollback();
        ex.printStackTrace();
        return false;
    } finally {
        em.close();
    }
}
    
    public List<Long> obtenerEspaciosOcupados(LocalDate fecha,
                                              LocalTime horaInicio,
                                              LocalTime horaFin) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Long> q = em.createQuery(
                "SELECT DISTINCT r.coworkingSpaceId.id " +
                "FROM Reservations r " +
                "WHERE r.reservationDate = :fecha " +
                  "AND r.startTime < :horaFin " +
                  "AND r.endTime > :horaInicio",
                Long.class
            );
            q.setParameter("fecha",     fecha);
            q.setParameter("horaInicio",horaInicio);
            q.setParameter("horaFin",   horaFin);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    
    public List<CoworkingSpaces> buscarEspaciosDisponibles(LocalDate fecha,
                                                           LocalTime horaInicio,
                                                           LocalTime horaFin,
                                                           int capacidadMinima) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            
            TypedQuery<Long> q1 = em.createQuery(
                "SELECT DISTINCT r.coworkingSpaceId.id " +
                "FROM Reservations r " +
                "WHERE r.reservationDate = :fecha " +
                  "AND r.startTime < :horaFin " +
                  "AND r.endTime > :horaInicio",
                Long.class
            );
            q1.setParameter("fecha",     fecha);
            q1.setParameter("horaInicio",horaInicio);
            q1.setParameter("horaFin",   horaFin);
            List<Long> ocupados = q1.getResultList();

            
            TypedQuery<CoworkingSpaces> q2 = em.createQuery(
                "SELECT c FROM CoworkingSpaces c " +
                "WHERE c.spaceId.id NOT IN :ocupados " +
                  "AND c.capacity >= :capacidad",
                CoworkingSpaces.class
            );
            q2.setParameter("ocupados",  ocupados.isEmpty() ? List.of(-1L) : ocupados);
            q2.setParameter("capacidad", capacidadMinima);
            return q2.getResultList();
        } finally {
            em.close();
        }
    }

 

public List<ReservationViewDto> listarTodasView() {
    var em = JPAUtil.getEntityManager();
    try {
        TypedQuery<Reservations> q = em.createQuery(
          "SELECT r FROM Reservations r", Reservations.class
        );
        return q.getResultList().stream()
                .map(ReservationViewDto::new)
                .collect(Collectors.toList());
    } finally {
        em.close();
    }
}

public List<ReservationViewDto> listarPorFiltros(LocalDate fecha,
                                                 Long userId,
                                                 String piso) {
    var em = JPAUtil.getEntityManager();
    try {
        StringBuilder sb = new StringBuilder("SELECT r FROM Reservations r WHERE 1=1");
        if (fecha  != null)   sb.append(" AND r.reservationDate = :fecha");
        if (userId != null)   sb.append(" AND r.userId.id        = :uid");
        if (piso   != null && !piso.isEmpty()) {
            
            sb.append(" AND r.coworkingSpaceId.spaceId.floor = :flr");
        }

        TypedQuery<Reservations> q = em.createQuery(sb.toString(), Reservations.class);
        if (fecha  != null)   q.setParameter("fecha", fecha);
        if (userId != null)   q.setParameter("uid",    userId);
        if (piso   != null && !piso.isEmpty()) {
            int num = Integer.parseInt(piso.substring(1));
            q.setParameter("flr", num);
        }

        return q.getResultList()
                .stream()
                .map(ReservationViewDto::new)
                .collect(Collectors.toList());
    } finally {
        em.close();
    }
}

    
    
    public boolean eliminarReserva(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Reservations r = em.find(Reservations.class, id);
            if (r != null) em.remove(r);
            em.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            return false;
        } finally {
            em.close();
        }
    }
    
    public boolean actualizarReserva(Long id, LocalDate fecha,
                                 LocalTime inicio, LocalTime fin) {
    EntityManager em = JPAUtil.getEMF().createEntityManager();
    try {
        em.getTransaction().begin();
        Reservations r = em.find(Reservations.class, id);
        if (r == null) return false;
        r.setReservationDate(fecha);
        r.setStartTime(inicio);
        r.setEndTime(fin);
        em.merge(r);
        em.getTransaction().commit();
        return true;
    } catch (Exception e) {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
        return false;
    } finally {
        em.close();
    }
}
   
public boolean hayColision(Long reservationId,
                           LocalDate fecha,
                           LocalTime inicio,
                           LocalTime fin) {
    var em = JPAUtil.getEntityManager();
    try {
        TypedQuery<Reservations> q = em.createQuery(
            "SELECT r FROM Reservations r " +
            " WHERE r.coworkingSpaceId.id = (" +
            "   SELECT r2.coworkingSpaceId.id FROM Reservations r2 WHERE r2.id = :rid" +
            ") AND r.reservationDate = :fecha " +
            " AND r.startTime < :fin AND r.endTime > :inicio " +
            " AND r.id <> :rid",
            Reservations.class
        );
        q.setParameter("rid", reservationId);
        q.setParameter("fecha", fecha);
        q.setParameter("inicio", inicio);
        q.setParameter("fin", fin);
        return !q.getResultList().isEmpty();
    } finally {
        em.close();
    }
}



}
