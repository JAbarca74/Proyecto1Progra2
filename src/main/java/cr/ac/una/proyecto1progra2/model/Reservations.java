package cr.ac.una.proyecto1progra2.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import javax.persistence.*;

@Entity
@Table(name = "TB_RESERVATIONS")
@NamedQueries({
  @NamedQuery(
    name="Reservations.findAll",
    query="SELECT r FROM Reservations r"
  ),
  @NamedQuery(
    name="Reservations.findByOverlap",
    query="SELECT r FROM Reservations r " +
          "WHERE r.spaceId = :spaceId " +
            "AND r.date = :date " +
            "AND r.startTime < :endTime " +
            "AND r.endTime > :startTime"
  )
})
public class Reservations implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;

    @Column(name="FIRST_NAME")
    private String firstName;

    @Column(name="LAST_NAME")
    private String lastName;

    @Column(name="SPACE_ID")
    private Long spaceId;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name="USER_ID")
private Usuarios userId;

    @Column(name="QUANTITY")
    private Integer quantity;

    @Column(name="DATE")
    private LocalDate date;

    @Column(name="START_TIME")
    private LocalTime startTime;

    @Column(name="END_TIME")
    private LocalTime endTime;

    @Column(name="PRICE")
    private BigDecimal price;

    @Version
    @Column(name="VERSION")
    private Long version;

    // --- getters & setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String fn) { this.firstName = fn; }

    public String getLastName() { return lastName; }
    public void setLastName(String ln) { this.lastName = ln; }

    public Long getSpaceId() { return spaceId; }
    public void setSpaceId(Long sid) { this.spaceId = sid; }

    public Usuarios getUserId() { return userId; }
    public void setUserId(Usuarios u) { this.userId = u; }

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
