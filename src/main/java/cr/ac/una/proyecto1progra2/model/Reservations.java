package cr.ac.una.proyecto1progra2.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "TB_RESERVATIONS")
@NamedQueries({
    @NamedQuery(name = "Reservations.findAll", query = "SELECT r FROM Reservations r"),
    @NamedQuery(name = "Reservations.findByOverlap", query = "SELECT r FROM Reservations r WHERE r.coworkingSpaceId = :space AND r.reservationDate = :date AND r.startTime < :endTime AND r.endTime > :startTime")
})
public class Reservations implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reservations_seq")
    @SequenceGenerator(name = "reservations_seq", sequenceName = "UNA.RESERVATIONS_SEQ", allocationSize = 1)
    @Column(name = "ID", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COWORKING_SPACE_ID", nullable = false)
    private CoworkingSpaces coworkingSpaceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private Usuarios userId;

    @Column(name = "RESERVATION_DATE")
    private LocalDate reservationDate;

    @Column(name = "START_TIME", nullable = false)
    private LocalTime startTime;

    @Column(name = "END_TIME", nullable = false)
    private LocalTime endTime;

    @Version
    @Column(name = "VERSION")
    private Long version;
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public CoworkingSpaces getCoworkingSpaceId() { return coworkingSpaceId; }
    public void setCoworkingSpaceId(CoworkingSpaces coworkingSpaceId) { this.coworkingSpaceId = coworkingSpaceId; }

    public Usuarios getUserId() { return userId; }
    public void setUserId(Usuarios userId) { this.userId = userId; }

    public LocalDate getReservationDate() { return reservationDate; }
    public void setReservationDate(LocalDate reservationDate) { this.reservationDate = reservationDate; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}
