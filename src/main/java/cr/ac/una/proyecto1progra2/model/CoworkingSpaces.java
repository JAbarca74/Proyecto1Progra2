/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.proyecto1progra2.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author jeffersonabarcap
 */
@Entity
@Table(name = "TB_COWORKING_SPACES")
@NamedQueries({
    @NamedQuery(name = "CoworkingSpaces.findAll", query = "SELECT c FROM CoworkingSpaces c"),
    @NamedQuery(name = "CoworkingSpaces.findById", query = "SELECT c FROM CoworkingSpaces c WHERE c.id = :id"),
    @NamedQuery(name = "CoworkingSpaces.findByName", query = "SELECT c FROM CoworkingSpaces c WHERE c.name = :name"),
    @NamedQuery(name = "CoworkingSpaces.findByCapacity", query = "SELECT c FROM CoworkingSpaces c WHERE c.capacity = :capacity")})
public class CoworkingSpaces implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;
    @Column(name = "CAPACITY")
    private BigInteger capacity;
    @JoinColumn(name = "SPACE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Spaces spaceId;
    @JoinColumn(name = "TYPE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private SpaceTypes typeId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "coworkingSpaceId")
    private Collection<Reservations> reservationsCollection;

    public CoworkingSpaces() {
    }

    public CoworkingSpaces(BigDecimal id) {
        this.id = id;
    }

    public CoworkingSpaces(BigDecimal id, String name) {
        this.id = id;
        this.name = name;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigInteger getCapacity() {
        return capacity;
    }

    public void setCapacity(BigInteger capacity) {
        this.capacity = capacity;
    }

    public Spaces getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Spaces spaceId) {
        this.spaceId = spaceId;
    }

    public SpaceTypes getTypeId() {
        return typeId;
    }

    public void setTypeId(SpaceTypes typeId) {
        this.typeId = typeId;
    }

    public Collection<Reservations> getReservationsCollection() {
        return reservationsCollection;
    }

    public void setReservationsCollection(Collection<Reservations> reservationsCollection) {
        this.reservationsCollection = reservationsCollection;
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
        if (!(object instanceof CoworkingSpaces)) {
            return false;
        }
        CoworkingSpaces other = (CoworkingSpaces) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cr.ac.una.proyecto1progra2.model.CoworkingSpaces[ id=" + id + " ]";
    }
    
}
