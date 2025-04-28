package cr.ac.una.proyecto1progra2.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class SpaceTypesDto {

    public SimpleStringProperty id;
    public SimpleStringProperty nombre;
    public SimpleObjectProperty<Boolean> estado;
    public Long version;
    private Boolean modificado;

    public SpaceTypesDto() {
        this.modificado = false;
        this.id = new SimpleStringProperty();
        this.nombre = new SimpleStringProperty();
        this.estado = new SimpleObjectProperty<>(true);
    }

    // 🚀 Constructor bien hecho que mapea la entidad SpaceTypes
    public SpaceTypesDto(SpaceTypes type) {
        this();
        this.id.set(type.getId() != null ? type.getId().toString() : null);
        this.nombre.set(type.getTypeName());
        this.version = type.getVersion();
    }

    public Long getId() {
        if (id.get() != null && !id.get().isEmpty()) {
            return Long.valueOf(id.get());
        } else {
            return null;
        }
    }

    public void setId(Long id) {
        this.id.set(id != null ? id.toString() : null);
    }

    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public Boolean getEstado() {
        return estado.get();
    }

    public void setEstado(Boolean estado) {
        this.estado.set(estado);
    }

    public Boolean getModificado() {
        return modificado;
    }

    public void setModificado(Boolean modificado) {
        this.modificado = modificado;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "SpaceTypesDto{" +
                "id=" + id.get() +
                ", nombre=" + nombre.get() +
                ", estado=" + estado.get() +
                '}';
    }
}
