package cr.ac.una.proyecto1progra2.DTO;

import cr.ac.una.proyecto1progra2.model.SpaceTypes;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class SpaceTypesDto {

    private final SimpleLongProperty   id        = new SimpleLongProperty();
    private final SimpleStringProperty typeName  = new SimpleStringProperty();
    private Long                       version;
    private boolean                    modificado = false;

    public SpaceTypesDto() {}

    /** Mapea desde la entidad */
    public SpaceTypesDto(SpaceTypes e) {
        setId(e.getId());
        setTypeName(e.getTypeName());
        this.version = e.getVersion();
    }

    // id
    public Long getId() { return id.get(); }
    public void setId(Long id) { this.id.set(id != null ? id : 0L); }
    public SimpleLongProperty idProperty() { return id; }

    // typeName
    public String getTypeName() { return typeName.get(); }
    public void setTypeName(String name) { this.typeName.set(name); }
    public SimpleStringProperty typeNameProperty() { return typeName; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    public boolean isModificado() { return modificado; }
    public void setModificado(boolean m) { this.modificado = m; }
}
