package cr.ac.una.proyecto1progra2.service;

import cr.ac.una.proyecto1progra2.model.Reservations;
import cr.ac.una.proyecto1progra2.model.ReservationsDto;
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

public class ReservationsService {

    EntityManager em = EntityManagerHelper.getInstance().getManager();
    private EntityTransaction et;

    public Respuesta getReservation(Long id) {
        try {
            Query qry = em.createNamedQuery("Reservations.findById", Reservations.class);
            qry.setParameter("id", id);
            ReservationsDto reservationDto = new ReservationsDto((Reservations) qry.getSingleResult());
            return new Respuesta(true, "", "", "Reservation", reservationDto);
        } catch (NoResultException ex) {
            return new Respuesta(false, "No existe la reservación con el id ingresado.", "getReservation NoResultException");
        } catch (Exception ex) {
            Logger.getLogger(ReservationsService.class.getName()).log(Level.SEVERE, "Error obteniendo la reservación [" + id + "]", ex);
            return new Respuesta(false, "Error obteniendo la reservación.", "getReservation " + ex.getMessage());
        }
    }

    public Respuesta guardarReservation(ReservationsDto reservationDto) {
        try {
            et = em.getTransaction();
            et.begin();
            Reservations reservation;
            if (reservationDto.getId() != null && reservationDto.getId() > 0) {
                reservation = em.find(Reservations.class, reservationDto.getId());
                if (reservation == null) {
                    et.rollback();
                    return new Respuesta(false, "No se encontró la reservación a modificar.", "guardarReservation NoResultException");
                }
                reservation.actualizar(reservationDto);
                reservation = em.merge(reservation);
            } else {
                reservation = new Reservations(reservationDto);
                em.persist(reservation);
            }
            et.commit();
            return new Respuesta(true, "", "", "Reservation", new ReservationsDto(reservation));
        } catch (Exception ex) {
            et.rollback();
            Logger.getLogger(ReservationsService.class.getName()).log(Level.SEVERE, "Ocurrió un error al guardar la reservación.", ex);
            return new Respuesta(false, "Ocurrió un error al guardar la reservación.", "guardarReservation " + ex.getMessage());
        }
    }

    public Respuesta eliminarReservation(Long id) {
        try {
            et = em.getTransaction();
            et.begin();
            Reservations reservation;
            if (id != null && id > 0) {
                reservation = em.find(Reservations.class, id);
                if (reservation == null) {
                    et.rollback();
                    return new Respuesta(false, "No se encontró la reservación a eliminar.", "eliminarReservation NoResultException");
                }
                em.remove(reservation);
            } else {
                et.rollback();
                return new Respuesta(false, "Debe cargar la reservación a eliminar.", "eliminarReservation NoResultException");
            }
            et.commit();
            return new Respuesta(true, "", "");
        } catch (Exception ex) {
            et.rollback();
            if (ex.getCause() != null && ex.getCause().getCause() instanceof SQLIntegrityConstraintViolationException) {
                return new Respuesta(false, "No se puede eliminar la reservación porque tiene relaciones con otros registros.", "eliminarReservation " + ex.getMessage());
            }
            Logger.getLogger(ReservationsService.class.getName()).log(Level.SEVERE, "Error eliminando la reservación.", ex);
            return new Respuesta(false, "Error eliminando la reservación.", "eliminarReservation " + ex.getMessage());
        }
    }

    public Respuesta listarReservations() {
        try {
            Query qry = em.createNamedQuery("Reservations.findAll", Reservations.class);
            List<Reservations> reservations = qry.getResultList();
            List<ReservationsDto> reservationsDto = new ArrayList<>();
            for (Reservations reservation : reservations) {
                reservationsDto.add(new ReservationsDto(reservation));
            }
            return new Respuesta(true, "", "", "Reservations", reservationsDto);
        } catch (Exception ex) {
            Logger.getLogger(ReservationsService.class.getName()).log(Level.SEVERE, "Error listando reservaciones.", ex);
            return new Respuesta(false, "Error listando reservaciones.", "listarReservations " + ex.getMessage());
        }
    }
}
