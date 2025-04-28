package cr.ac.una.proyecto1progra2.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "TB_SPACE_TYPES")
@NamedQueries({
    @NamedQuery(name = "SpaceTypes.findAll", query = "SELECT s FROM SpaceTypes s"),
    @NamedQuery(name = "SpaceTypes.findById", query = "SELECT s FROM SpaceTypes s WHERE s.id = :id"),
    @NamedQuery(name = "SpaceTypes.findByTypeName", query = "SELECT s FROM SpaceTypes s WHERE UPPER(s.typeName) = :typeName", hints = @QueryHint(name = "eclipselink.refresh", value = "true"))
})
public class SpaceTypes implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;

    @Basic(optional = false)
    @Column(name = "TYPE_NAME")
    private String typeName;

    @Version
    @Basic(optional = false)
    @Column(name = "VERSION")
    private Long version;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "typeId", fetch = FetchType.LAZY)
    private Collection<CoworkingSpaces> coworkingSpacesCollection;

    public SpaceTypes() {
    }

    public SpaceTypes(Long id) {
        this.id = id;
    }

    public SpaceTypes(SpaceTypesDto spaceTypesDto) {
        this();
        this.id = spaceTypesDto.getId();
        actualizar(spaceTypesDto);
    }

    public void actualizar(SpaceTypesDto spaceTypesDto) {
        this.typeName = spaceTypesDto.getNombre();
        this.version = spaceTypesDto.getVersion();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SpaceTypes)) {
            return false;
        }
        SpaceTypes other = (SpaceTypes) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "cr.ac.una.proyecto1progra2.model.SpaceTypes[ id=" + id + " ]";
    }
}
