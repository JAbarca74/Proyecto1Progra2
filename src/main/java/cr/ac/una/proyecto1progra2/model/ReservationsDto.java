package cr.ac.una.proyecto1progra2.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationsDto {
    private Long id;
    private String firstName;
    private String lastName;
    private Long   spaceId;
    private Integer quantity;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private BigDecimal price;
    private Long version;

    public ReservationsDto() {}

    public ReservationsDto(Reservations r) {
        this.id         = r.getId();
        this.firstName  = r.getFirstName();
        this.lastName   = r.getLastName();
        this.spaceId    = r.getSpaceId();
        this.quantity   = r.getQuantity();
        this.date       = r.getDate();
        this.startTime  = r.getStartTime();
        this.endTime    = r.getEndTime();
        this.price      = r.getPrice();
        this.version    = r.getVersion();
    }

    // --- getters & setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String fn) { this.firstName = fn; }

    public String getLastName() { return lastName; }
    public void setLastName(String ln) { this.lastName = ln; }

    public Long getSpaceId() { return spaceId; }
    public void setSpaceId(Long sid) { this.spaceId = sid; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer q) { this.quantity = q; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate d) { this.date = d; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime st) { this.startTime = st; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime et) { this.endTime = et; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal p) { this.price = p; }

    public Long getVersion() { return version; }
    public void setVersion(Long v) { this.version = v; }
}
