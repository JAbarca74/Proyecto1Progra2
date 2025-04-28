package cr.ac.una.proyecto1progra2.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class SpacesDto {

    public SimpleStringProperty id;
    public SimpleStringProperty nombre;
    public SimpleStringProperty capacidad;
    public SimpleObjectProperty<Boolean> estado;
    public Long version;
    private Boolean modificado;

    public SpacesDto() {
        this.modificado = false;
        this.id = new SimpleStringProperty();
        this.nombre = new SimpleStringProperty();
        this.capacidad = new SimpleStringProperty();
        this.estado = new SimpleObjectProperty<>(true);
    }

    // 🚀 Constructor que mapea directamente desde la entidad Spaces
    public SpacesDto(Spaces space) {
        this();
        this.id.set(space.getId() != null ? space.getId().toString() : null);
        this.nombre.set(space.getName());
        this.capacidad.set(space.getCapacity() != null ? space.getCapacity().toString() : null);
        this.version = space.getVersion();
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

    public Integer getCapacidad() {
        if (capacidad.get() != null && !capacidad.get().isEmpty()) {
            return Integer.valueOf(capacidad.get());
        } else {
            return null;
        }
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad.set(capacidad != null ? capacidad.toString() : null);
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
        return "SpacesDto{" +
                "id=" + id.get() +
                ", nombre=" + nombre.get() +
                ", capacidad=" + capacidad.get() +
                ", estado=" + estado.get() +
                '}';
    }
}
