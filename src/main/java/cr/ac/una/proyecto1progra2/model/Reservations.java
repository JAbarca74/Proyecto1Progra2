/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.proyecto1progra2.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author jeffersonabarcap
 */
@Entity
@Table(name = "TB_RESERVATIONS")
@NamedQueries({
    @NamedQuery(name = "Reservations.findAll", query = "SELECT r FROM Reservations r"),
    @NamedQuery(name = "Reservations.findById", query = "SELECT r FROM Reservations r WHERE r.id = :id"),
    @NamedQuery(name = "Reservations.findByStartTime", query = "SELECT r FROM Reservations r WHERE r.startTime = :startTime"),
    @NamedQuery(name = "Reservations.findByEndTime", query = "SELECT r FROM Reservations r WHERE r.endTime = :endTime"),
    @NamedQuery(name = "Reservations.findByIsCancelled", query = "SELECT r FROM Reservations r WHERE r.isCancelled = :isCancelled")})
public class Reservations implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @Column(name = "START_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;
    @Basic(optional = false)
    @Column(name = "END_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;
    @Column(name = "IS_CANCELLED")
    private Character isCancelled;
    @JoinColumn(name = "COWORKING_SPACE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private CoworkingSpaces coworkingSpaceId;
    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Usuarios userId;

    public Reservations() {
    }

    public Reservations(BigDecimal id) {
        this.id = id;
    }

    public Reservations(BigDecimal id, Date startTime, Date endTime) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
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

    public Character getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(Character isCancelled) {
        this.isCancelled = isCancelled;
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
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reservations)) {
            return false;
        }
        Reservations other = (Reservations) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cr.ac.una.proyecto1progra2.model.Reservations[ id=" + id + " ]";
    }
    
}
