package cr.ac.una.proyecto1progra2.DTO;

import cr.ac.una.proyecto1progra2.model.Reservations;
import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationsDto {

    private Long id;
    private Long coworkingSpaceId;
    private LocalDate reservationDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long version;

    public ReservationsDto() {}

    public ReservationsDto(Reservations r) {
        this.id               = r.getId();
        this.coworkingSpaceId = r.getCoworkingSpaceId();
        this.reservationDate = r.getReservationDate();
        this.startTime       = r.getStartTime();
        this.endTime         = r.getEndTime();
        this.version         = r.getVersion();
    }

    // --- Getters y Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCoworkingSpaceId() { return coworkingSpaceId; }
    public void setCoworkingSpaceId(Long coworkingSpaceId) { this.coworkingSpaceId = coworkingSpaceId; }

    public LocalDate getReservationDate() { return reservationDate; }
    public void setReservationDate(LocalDate reservationDate) { this.reservationDate = reservationDate; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}