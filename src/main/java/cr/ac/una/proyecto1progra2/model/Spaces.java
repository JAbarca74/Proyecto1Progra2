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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author jeffersonabarcap
 */
@Entity
@Table(name = "TB_SPACES")
@NamedQueries({
    @NamedQuery(name = "Spaces.findAll", query = "SELECT s FROM Spaces s"),
    @NamedQuery(name = "Spaces.findById", query = "SELECT s FROM Spaces s WHERE s.id = :id"),
    @NamedQuery(name = "Spaces.findByName", query = "SELECT s FROM Spaces s WHERE s.name = :name"),
    @NamedQuery(name = "Spaces.findByCapacity", query = "SELECT s FROM Spaces s WHERE s.capacity = :capacity")})
public class Spaces implements Serializable {

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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "spaceId")
    private Collection<CoworkingSpaces> coworkingSpacesCollection;

    public Spaces() {
    }

    public Spaces(BigDecimal id) {
        this.id = id;
    }

    public Spaces(BigDecimal id, String name) {
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

    public Collection<CoworkingSpaces> getCoworkingSpacesCollection() {
        return coworkingSpacesCollection;
    }

    public void setCoworkingSpacesCollection(Collection<CoworkingSpaces> coworkingSpacesCollection) {
        this.coworkingSpacesCollection = coworkingSpacesCollection;
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
        if (!(object instanceof Spaces)) {
            return false;
        }
        Spaces other = (Spaces) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cr.ac.una.proyecto1progra2.model.Spaces[ id=" + id + " ]";
    }
    
}
