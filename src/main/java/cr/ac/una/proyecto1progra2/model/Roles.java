package cr.ac.una.proyecto1progra2.model;

import cr.ac.una.proyecto1progra2.DTO.RolesDto;
import java.io.Serializable;
import javax.persistence.*;
import javax.persistence.QueryHint;

@Entity
@Table(name = "TB_ROLES")
@NamedQueries({
    @NamedQuery(name = "Roles.findAll",  query = "SELECT r FROM Roles r"),
    @NamedQuery(name = "Roles.findById", query = "SELECT r FROM Roles r WHERE r.id = :id"),
    @NamedQuery(
      name = "Roles.findByName",
      query = "SELECT r FROM Roles r WHERE UPPER(r.name) = :name",
      hints = @QueryHint(name = "eclipselink.refresh", value = "true")
    )
})
public class Roles implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID") private Long id;

    @Column(name = "NAME", nullable = false) private String name;

    @Version
    @Column(name = "VERSION") private Long version;

    public Roles() {}
    public Roles(Long id) { this.id = id; }

    public Roles(RolesDto dto) {
        this.id = dto.getId();
        actualizar(dto);
    }

    public void actualizar(RolesDto dto) {
        this.name = dto.getNombre();
        this.version = dto.getVersion();
    }

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    @Override
    public int hashCode() { return id != null ? id.hashCode() : 0; }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Roles)) return false;
        Roles other = (Roles) obj;
        return id != null && id.equals(other.id);
    }

    @Override
    public String toString() {
        return name; 
    }
}
