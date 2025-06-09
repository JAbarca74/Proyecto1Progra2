package cr.ac.una.proyecto1progra2.model;

import cr.ac.una.proyecto1progra2.DTO.SpaceTypesDto;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "space_types_seq_gen")
    @SequenceGenerator(name = "space_types_seq_gen", sequenceName = "SPACE_TYPES_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TYPE_NAME")
    private String typeName;

    public SpaceTypes() {}

    public SpaceTypes(SpaceTypesDto dto) {
        this.id = dto.getId();
        actualizar(dto);
    }

    public void actualizar(SpaceTypesDto dto) {
        this.typeName = dto.getTypeName();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTypeName() { return typeName; }
    public void setTypeName(String typeName) { this.typeName = typeName; }
}