/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cr.ac.una.proyecto1progra2.model;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "TB_SPACE_TYPES")
@NamedQueries({
    @NamedQuery(name = "SpaceTypes.findAll", query = "SELECT s FROM SpaceTypes s"),
    @NamedQuery(name = "SpaceTypes.findById", query = "SELECT s FROM SpaceTypes s WHERE s.id = :id"),
    @NamedQuery(name = "SpaceTypes.findByTypeName", query = "SELECT s FROM SpaceTypes s WHERE s.typeName = :typeName")})
public class SpaceTypes implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private BigDecimal id;
    @Basic(optional = false)
    @Column(name = "TYPE_NAME")
    private String typeName;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "typeId")
    private Collection<CoworkingSpaces> coworkingSpacesCollection;

    public SpaceTypes() {
    }

    public SpaceTypes(BigDecimal id) {
        this.id = id;
    }

    public SpaceTypes(BigDecimal id, String typeName) {
        this.id = id;
        this.typeName = typeName;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
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
        if (!(object instanceof SpaceTypes)) {
            return false;
        }
        SpaceTypes other = (SpaceTypes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "cr.ac.una.proyecto1progra2.model.SpaceTypes[ id=" + id + " ]";
    }
    
}
