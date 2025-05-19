package cr.ac.una.proyecto1progra2.service;

import cr.ac.una.proyecto1progra2.model.Reservations;
import cr.ac.una.proyecto1progra2.model.ReservationsDto;
import cr.ac.una.proyecto1progra2.util.EntityManagerHelper;
import cr.ac.una.proyecto1progra2.util.Respuesta;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationsService {
    private final EntityManager em = EntityManagerHelper.getInstance().getManager();
    private EntityTransaction et;

    public Respuesta listarReservas() {
        try {
            List<ReservationsDto> datos = em.createNamedQuery("Reservations.findAll", Reservations.class)
                .getResultList()
                .stream()
                .map(ReservationsDto::new)
                .collect(Collectors.toList());
            return new Respuesta(true, "", "", "Reservas", datos);
        } catch (Exception ex) {
            return new Respuesta(false, "Error listando reservas.", ex.getMessage());
        }
    }

    public boolean disponible(Long spaceId, LocalDate date,
                              LocalTime start, LocalTime end) {
        TypedQuery<Reservations> q = em.createNamedQuery("Reservations.findByOverlap", Reservations.class);
        q.setParameter("spaceId", spaceId);
        q.setParameter("date", date);
        q.setParameter("startTime", start);
        q.setParameter("endTime", end);
        return q.getResultList().isEmpty();
    }

    public Respuesta guardarReserva(ReservationsDto dto) {
        try {
            if (!disponible(dto.getSpaceId(), dto.getDate(), dto.getStartTime(), dto.getEndTime())) {
                return new Respuesta(false,
                    "El espacio no está disponible en esa franja.", "disponibilidad");
            }

            et = em.getTransaction();
            et.begin();

            Reservations r;
            if (dto.getId() != null) {
                r = em.find(Reservations.class, dto.getId());
                if (r == null) {
                    et.rollback();
                    return new Respuesta(false, "Reserva no encontrada.", "guardarReserva");
                }
            } else {
                r = new Reservations();
            }

            r.setFirstName(dto.getFirstName());
            r.setLastName(dto.getLastName());
            r.setSpaceId(dto.getSpaceId());
            r.setQuantity(dto.getQuantity());
            r.setDate(dto.getDate());
            r.setStartTime(dto.getStartTime());
            r.setEndTime(dto.getEndTime());
            r.setPrice(dto.getPrice());

            if (dto.getId() == null) em.persist(r);
            else                   em.merge(r);

            et.commit();
            return new Respuesta(true, "", "", "Reserva", new ReservationsDto(r));
        } catch (Exception ex) {
            if (et != null && et.isActive()) et.rollback();
            return new Respuesta(false, "Error guardando reserva.", ex.getMessage());
        }
    }
}
