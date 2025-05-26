package cr.ac.una.proyecto1progra2.model;

import cr.ac.una.proyecto1progra2.DTO.CoworkingSpacesDto;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "TB_COWORKING_SPACES")
@NamedQueries({
    @NamedQuery(name = "CoworkingSpaces.findAll", query = "SELECT c FROM CoworkingSpaces c"),
    @NamedQuery(name = "CoworkingSpaces.findById", query = "SELECT c FROM CoworkingSpaces c WHERE c.id = :id"),
    @NamedQuery(name = "CoworkingSpaces.findByName", query = "SELECT c FROM CoworkingSpaces c WHERE UPPER(c.name) = :name"),
    @NamedQuery(name = "CoworkingSpaces.findByCapacity", query = "SELECT c FROM CoworkingSpaces c WHERE c.capacity = :capacity")
})
public class CoworkingSpaces implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;

    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;

    @Column(name = "CAPACITY")
    private Integer capacity;

    @Version
    @Basic(optional = false)
    @Column(name = "VERSION")
    private Long version;

    @JoinColumn(name = "SPACE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Spaces spaceId;

    @JoinColumn(name = "TYPE_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private SpaceTypes typeId;    


    public CoworkingSpaces() {
    }

    public CoworkingSpaces(Long id) {
        this.id = id;
    }

    public CoworkingSpaces(CoworkingSpacesDto coworkingSpacesDto) {
        this();
        this.id = coworkingSpacesDto.getId();
        actualizar(coworkingSpacesDto);
    }

    public void actualizar(CoworkingSpacesDto coworkingSpacesDto) {
        this.name = coworkingSpacesDto.getNombre();
        this.capacity = coworkingSpacesDto.getCapacidad();
        this.version = coworkingSpacesDto.getVersion();
        // Nota: spaceId y typeId deben actualizarse en el Service si quieres cambiar las relaciones
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
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
        if (!(object instanceof CoworkingSpaces)) {
            return false;
        }
        CoworkingSpaces other = (CoworkingSpaces) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "cr.ac.una.proyecto1progra2.model.CoworkingSpaces[ id=" + id + " ]";
    }
}
