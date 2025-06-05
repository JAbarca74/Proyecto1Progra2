package cr.ac.una.proyecto1progra2.service;

import cr.ac.una.proyecto1progra2.model.CoworkingSpaces;
import cr.ac.una.proyecto1progra2.model.Reservations;
import cr.ac.una.proyecto1progra2.model.Usuarios;
import cr.ac.una.proyecto1progra2.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ReservationsService {

    public List<Reservations> buscarReservasTraslapadas(Long coworkingSpaceId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Reservations> traslapadas = null;
        try {
            TypedQuery<Reservations> query = em.createQuery(
                "SELECT r FROM Reservations r WHERE r.coworkingSpaceId.id = :spaceId AND r.reservationDate = :fecha " +
                "AND r.startTime < :horaFin AND r.endTime > :horaInicio", Reservations.class);
            query.setParameter("spaceId", coworkingSpaceId);
            query.setParameter("fecha", fecha);
            query.setParameter("horaInicio", horaInicio);
            query.setParameter("horaFin", horaFin);
            traslapadas = query.getResultList();
        } finally {
            em.close();
        }
        return traslapadas;
    }

   public boolean guardarReserva(Long userId, Long coworkingSpaceId, LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
    EntityManager em = JPAUtil.getEntityManager();
    try {
        em.getTransaction().begin();
        Reservations reserva = new Reservations();
        reserva.setUserId(em.getReference(Usuarios.class, userId));
        reserva.setCoworkingSpaceId(em.getReference(CoworkingSpaces.class, coworkingSpaceId));
        reserva.setReservationDate(fecha);
        reserva.setStartTime(horaInicio);
        reserva.setEndTime(horaFin);
        em.persist(reserva);
        em.getTransaction().commit();

        System.out.println("✅ Reserva guardada con éxito:");
        System.out.println("   - Usuario ID: " + userId);
        System.out.println("   - Espacio ID: " + coworkingSpaceId);
        System.out.println("   - Fecha: " + fecha);
        System.out.println("   - Hora inicio: " + horaInicio);
        System.out.println("   - Hora fin: " + horaFin);
        return true;
    } catch (Exception e) {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
        System.out.println("❌ ERROR al guardar la reserva:");
        System.out.println("   - Usuario ID: " + userId);
        System.out.println("   - Espacio ID: " + coworkingSpaceId);
        System.out.println("   - Fecha: " + fecha);
        System.out.println("   - Hora inicio: " + horaInicio);
        System.out.println("   - Hora fin: " + horaFin);
        e.printStackTrace();
        return false;
    } finally {
        em.close();
    }
}
    public List<Long> obtenerEspaciosOcupados(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        EntityManager em = JPAUtil.getEntityManager();
        List<Long> ids = null;
        try {
            TypedQuery<Long> query = em.createQuery(
                "SELECT DISTINCT r.coworkingSpaceId.id FROM Reservations r WHERE r.reservationDate = :fecha " +
                "AND r.startTime < :horaFin AND r.endTime > :horaInicio", Long.class);
            query.setParameter("fecha", fecha);
            query.setParameter("horaInicio", horaInicio);
            query.setParameter("horaFin", horaFin);
            ids = query.getResultList();
        } finally {
            em.close();
        }
        return ids;
    }

    public List<CoworkingSpaces> buscarEspaciosDisponibles(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, int capacidadMinima) {
        EntityManager em = JPAUtil.getEntityManager();
        List<CoworkingSpaces> disponibles;
        try {
            TypedQuery<Long> queryReservados = em.createQuery(
                "SELECT DISTINCT r.coworkingSpaceId.id FROM Reservations r WHERE r.reservationDate = :fecha " +
                "AND r.startTime < :horaFin AND r.endTime > :horaInicio", Long.class);
            queryReservados.setParameter("fecha", fecha);
            queryReservados.setParameter("horaInicio", horaInicio);
            queryReservados.setParameter("horaFin", horaFin);
            List<Long> ocupados = queryReservados.getResultList();

            TypedQuery<CoworkingSpaces> queryDisponibles = em.createQuery(
                "SELECT c FROM CoworkingSpaces c WHERE c.spaceId.id NOT IN :ocupados AND c.capacity >= :capacidad", CoworkingSpaces.class);
            queryDisponibles.setParameter("ocupados", ocupados.isEmpty() ? List.of(-1L) : ocupados);
            queryDisponibles.setParameter("capacidad", capacidadMinima);
            disponibles = queryDisponibles.getResultList();
        } finally {
            em.close();
        }
        return disponibles;
    }
}