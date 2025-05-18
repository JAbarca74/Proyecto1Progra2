package cr.ac.una.proyecto1progra2.model;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "TB_SPACE_TYPES")
@NamedQueries({
    @NamedQuery(name = "SpaceTypes.findAll", query = "SELECT s FROM SpaceTypes s"),
    @NamedQuery(name = "SpaceTypes.findById", query = "SELECT s FROM SpaceTypes s WHERE s.id = :id")
})
public class SpaceTypes implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TYPE_NAME")
    private String typeName;

    @Version
    @Column(name = "VERSION")
    private Long version;

    public SpaceTypes() {}

    /** Para crear desde un DTO */
    public SpaceTypes(SpaceTypesDto dto) {
        this.id = dto.getId();
        actualizar(dto);
    }

    /** Actualiza campos desde el DTO */
    public void actualizar(SpaceTypesDto dto) {
        this.typeName = dto.getTypeName();
        this.version  = dto.getVersion();
    }

    // Getters / setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
}
