package cr.ac.una.proyecto1progra2.service;

import cr.ac.una.proyecto1progra2.model.Reservations;
import cr.ac.una.proyecto1progra2.model.ReservationsDto;
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

public class ReservationsService {

    private final EntityManager em = EntityManagerHelper.getInstance().getManager();
    private EntityTransaction et;

    public Respuesta listarReservas() {
        try {
            TypedQuery<Reservations> qry = em.createNamedQuery("Reservations.findAll", Reservations.class);
            List<Reservations> list = qry.getResultList();
            List<ReservationsDto> dto = new ArrayList<>();
            for (Reservations r : list) {
                dto.add(new ReservationsDto(r));
            }
            return new Respuesta(true, "", "", "Reservas", dto);
        } catch (Exception ex) {
            Logger.getLogger(ReservationsService.class.getName()).log(Level.SEVERE, "Error listando reservas.", ex);
            return new Respuesta(false, "Error listando reservas.", ex.getMessage());
        }
    }

    public Respuesta getReserva(Long id) {
        try {
            Reservations r = em.find(Reservations.class, id);
            if (r == null) {
                return new Respuesta(false, "No existe la reserva con ese id.", "");
            }
            return new Respuesta(true, "", "", "Reserva", new ReservationsDto(r));
        } catch (Exception ex) {
            Logger.getLogger(ReservationsService.class.getName()).log(Level.SEVERE, "Error obteniendo reserva [" + id + "]", ex);
            return new Respuesta(false, "Error obteniendo reserva.", ex.getMessage());
        }
    }

    public Respuesta guardarReserva(ReservationsDto dto) {
        try {
            et = em.getTransaction();
            et.begin();
            Reservations r;
            if (dto.getId() != null && dto.getId() > 0) {
                r = em.find(Reservations.class, dto.getId());
                if (r == null) {
                    et.rollback();
                    return new Respuesta(false, "No se encontró la reserva a modificar.", "");
                }
                r.actualizar(dto);
                r = em.merge(r);
            } else {
                r = new Reservations(dto);
                em.persist(r);
            }
            et.commit();
            return new Respuesta(true, "", "", "Reserva", new ReservationsDto(r));
        } catch (Exception ex) {
            if (et.isActive()) et.rollback();
            Logger.getLogger(ReservationsService.class.getName()).log(Level.SEVERE, "Error guardando reserva.", ex);
            return new Respuesta(false, "Error guardando reserva.", ex.getMessage());
        }
    }

    public Respuesta eliminarReserva(Long id) {
        try {
            et = em.getTransaction();
            et.begin();
            Reservations r = em.find(Reservations.class, id);
            if (r == null) {
                et.rollback();
                return new Respuesta(false, "No se encontró la reserva a eliminar.", "");
            }
            em.remove(r);
            et.commit();
            return new Respuesta(true, "", "");
        } catch (Exception ex) {
            if (et.isActive()) et.rollback();
            Logger.getLogger(ReservationsService.class.getName()).log(Level.SEVERE, "Error eliminando reserva.", ex);
            return new Respuesta(false, "Error eliminando reserva.", ex.getMessage());
        }
    }
}
