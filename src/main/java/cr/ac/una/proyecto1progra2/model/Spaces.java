package cr.ac.una.proyecto1progra2.model;

import cr.ac.una.proyecto1progra2.DTO.SpacesDto;
import java.io.Serializable;
import java.math.BigInteger;
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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TB_SPACES")
@NamedQueries({
    @NamedQuery(name = "Spaces.findAll", query = "SELECT s FROM Spaces s"),
    @NamedQuery(name = "Spaces.findById", query = "SELECT s FROM Spaces s WHERE s.id = :id"),
    @NamedQuery(name = "Spaces.findByName", query = "SELECT s FROM Spaces s WHERE UPPER(s.name) = :name", hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
    @NamedQuery(name = "Spaces.findByCapacity", query = "SELECT s FROM Spaces s WHERE s.capacity = :capacity")
})
public class Spaces implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "spaces_seq")
    @SequenceGenerator(name = "spaces_seq", sequenceName = "UNA.SPACES_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;

    @Column(name = "CAPACITY")
    private BigInteger capacity;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "spaceId", fetch = FetchType.LAZY)
    private Collection<CoworkingSpaces> coworkingSpacesCollection;

    public Spaces() {
    }

    public Spaces(Long id) {
        this.id = id;
    }

    public Spaces(SpacesDto spacesDto) {
        this();
        this.id = spacesDto.getId();
        actualizar(spacesDto);
    }

    public void actualizar(SpacesDto spacesDto) {
        this.name = spacesDto.getNombre();
        this.capacity = spacesDto.getCapacidad() != null ? BigInteger.valueOf(spacesDto.getCapacidad()) : null;
        // Quité la línea que usaba version porque no existe en la tabla
        // this.version = spacesDto.getVersion();
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
        if (!(object instanceof Spaces)) {
            return false;
        }
        Spaces other = (Spaces) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "cr.ac.una.proyecto1progra2.model.Spaces[ id=" + id + " ]";
    }
}