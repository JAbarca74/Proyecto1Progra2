package cr.ac.una.proyecto1progra2.DTO;

import cr.ac.una.proyecto1progra2.model.Roles;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class RolesDto {

    private final SimpleLongProperty id     = new SimpleLongProperty();
    private final SimpleStringProperty nombre = new SimpleStringProperty();
    private Long version;
    private boolean modificado = false;

    public RolesDto() {}

    public RolesDto(Roles rol) {
        setId(rol.getId());
        setNombre(rol.getName());
        setVersion(rol.getVersion());
    }

    public Long getId() { return id.get(); }
    public void setId(Long v) { id.set(v); }

    public String getNombre() { return nombre.get(); }
    public void setNombre(String s) { nombre.set(s); }

    public Long getVersion() { return version; }
    public void setVersion(Long v) { version = v; }

    public boolean isModificado() { return modificado; }
    public void setModificado(boolean m) { modificado = m; }

    @Override
    public String toString() {
        return nombre.get(); 
    }
}
