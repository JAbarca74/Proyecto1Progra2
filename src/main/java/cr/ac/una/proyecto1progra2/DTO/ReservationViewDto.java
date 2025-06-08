package cr.ac.una.proyecto1progra2.DTO;

import cr.ac.una.proyecto1progra2.model.Reservations;
import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationViewDto {
    private Long id;
    private String userName;
    private String spaceName;
    private LocalDate reservationDate;
    private LocalTime startTime;
    private LocalTime endTime;

    public ReservationViewDto(Reservations r) {
        this.id              = r.getId();
        this.userName        = r.getUserId().getUsername();
        this.spaceName       = r.getCoworkingSpaceId().getName();
        this.reservationDate = r.getReservationDate();
        this.startTime       = r.getStartTime();
        this.endTime         = r.getEndTime();
    }

    public Long getId()               { return id; }
    public String getUserName()       { return userName; }
    public String getSpaceName()      { return spaceName; }
    public LocalDate getReservationDate() { return reservationDate; }
    public LocalTime getStartTime()   { return startTime; }
    public LocalTime getEndTime()     { return endTime; }

    /** Extrae “P0”–“P3” del nombre del CoworkingSpace */
    public String getPiso() {
        if (spaceName == null) return "";
        String up = spaceName.toUpperCase();
        if (up.contains("P0")) return "P0";
        if (up.contains("P1")) return "P1";
        if (up.contains("P2")) return "P2";
        if (up.contains("P3")) return "P3";
        return "";
    }

    /** Estado estático (si no manejas cancelación) */
    public String getEstado() {
        return "Activa";
    }
}
