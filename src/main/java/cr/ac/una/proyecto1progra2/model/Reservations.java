package cr.ac.una.proyecto1progra2.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(name = "TB_RESERVATIONS")
@NamedQueries({
    @NamedQuery(name = "Reservations.findAll", query = "SELECT r FROM Reservations r"),
    @NamedQuery(name = "Reservations.findById", query = "SELECT r FROM Reservations r WHERE r.id = :id"),
    @NamedQuery(name = "Reservations.findByStartTime", query = "SELECT r FROM Reservations r WHERE r.startTime = :startTime"),
    @NamedQuery(name = "Reservations.findByEndTime", query = "SELECT r FROM Reservations r WHERE r.endTime = :endTime"),
    @NamedQuery(name = "Reservations.findByIsCancelled", query = "SELECT r FROM Reservations r WHERE r.isCancelled = :isCancelled")
})
public class Reservations implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;

    @Basic(optional = false)
    @Column(name = "START_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Basic(optional = false)
    @Column(name = "END_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @Basic(optional = false)
    @Column(name = "IS_CANCELLED")
    private String isCancelled; // "S" o "N"

    @Version
    @Basic(optional = false)
    @Column(name = "VERSION")
    private Long version;

    @JoinColumn(name = "COWORKING_SPACE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private CoworkingSpaces coworkingSpaceId;

    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Usuarios userId;

    public Reservations() {
    }

    public Reservations(Long id) {
        this.id = id;
    }

    public Reservations(ReservationsDto reservationsDto) {
        this();
        this.id = reservationsDto.getId();
        actualizar(reservationsDto);
    }

    public void actualizar(ReservationsDto reservationsDto) {
        this.startTime = java.sql.Timestamp.valueOf(reservationsDto.getStartTime().atStartOfDay());
        this.endTime = java.sql.Timestamp.valueOf(reservationsDto.getEndTime().atStartOfDay());
        this.isCancelled = reservationsDto.getIsCancelled();
        this.version = reservationsDto.getVersion();
        // El coworkingSpaceId y userId se deberían setear aparte en el service, buscando sus entidades por ID
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(String isCancelled) {
        this.isCancelled = isCancelled;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public CoworkingSpaces getCoworkingSpaceId() {
        return coworkingSpaceId;
    }

    public void setCoworkingSpaceId(CoworkingSpaces coworkingSpaceId) {
        this.coworkingSpaceId = coworkingSpaceId;
    }

    public Usuarios getUserId() {
        return userId;
    }

    public void setUserId(Usuarios userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Reservations)) {
            return false;
        }
        Reservations other = (Reservations) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "cr.ac.una.proyecto1progra2.model.Reservations[ id=" + id + " ]";
    }
}
