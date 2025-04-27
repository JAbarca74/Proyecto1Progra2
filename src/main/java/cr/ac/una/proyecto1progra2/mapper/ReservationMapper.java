package cr.ac.una.proyecto1progra2.mapper;

import cr.ac.una.proyecto1progra2.dto.ReservationDTO;
import cr.ac.una.proyecto1progra2.model.Reservations;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class ReservationMapper {
    public static ReservationDTO toDTO(Reservations r) {
        if (r == null) return null;
        ReservationDTO dto = new ReservationDTO();
        dto.setId(r.getId().longValue());
        dto.setUserId(r.getUserId().getId().longValue());
        dto.setCoworkingSpaceId(r.getCoworkingSpaceId().getId().longValue());
        dto.setStartTime(LocalDateTime.ofInstant(
            r.getStartTime().toInstant(), ZoneId.systemDefault()));
        dto.setEndTime(LocalDateTime.ofInstant(
            r.getEndTime().toInstant(), ZoneId.systemDefault()));
        dto.setCancelled(r.getIsCancelled() != null && r.getIsCancelled() == 'Y');
        return dto;
    }
}
