package cr.ac.una.proyecto1progra2.DTO;

import cr.ac.una.proyecto1progra2.model.CoworkingSpaces;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;

public class CoworkingSpacesDto {

    public SimpleStringProperty id;
    public SimpleStringProperty nombre;
    public SimpleStringProperty capacidad;
    public SimpleStringProperty spaceId;
    public SimpleStringProperty typeId;
    public SimpleObjectProperty<Boolean> estado;
    public Long version;
    private Boolean modificado;

    public CoworkingSpacesDto() {
        this.modificado = false;
        this.id = new SimpleStringProperty();
        this.nombre = new SimpleStringProperty();
        this.capacidad = new SimpleStringProperty();
        this.spaceId = new SimpleStringProperty();
        this.typeId = new SimpleStringProperty();
        this.estado = new SimpleObjectProperty<>(true);
    }

    // 🚀 Este constructor ahora sí FUNCIONA (arreglado)
    public CoworkingSpacesDto(CoworkingSpaces coworkingSpace) {
        this();
        this.id.set(coworkingSpace.getId() != null ? coworkingSpace.getId().toString() : null);
        this.nombre.set(coworkingSpace.getName());
        this.capacidad.set(coworkingSpace.getCapacity() != null ? coworkingSpace.getCapacity().toString() : null);
        this.spaceId.set(coworkingSpace.getSpaceId() != null ? coworkingSpace.getSpaceId().getId().toString() : null);
        this.typeId.set(coworkingSpace.getTypeId() != null ? coworkingSpace.getTypeId().getId().toString() : null);
        this.version = coworkingSpace.getVersion();
    }

    public Long getId() {
        if (id.get() != null && !id.get().isEmpty())
            return Long.valueOf(id.get());
        else
            return null;
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
        if (capacidad.get() != null && !capacidad.get().isEmpty())
            return Integer.valueOf(capacidad.get());
        else
            return null;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad.set(capacidad != null ? capacidad.toString() : null);
    }

    public Long getSpaceId() {
        if (spaceId.get() != null && !spaceId.get().isEmpty())
            return Long.valueOf(spaceId.get());
        else
            return null;
    }

    public void setSpaceId(Long id) {
        this.spaceId.set(id != null ? id.toString() : null);
    }

    public Long getTypeId() {
        if (typeId.get() != null && !typeId.get().isEmpty())
            return Long.valueOf(typeId.get());
        else
            return null;
    }

    public void setTypeId(Long id) {
        this.typeId.set(id != null ? id.toString() : null);
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
        return "CoworkingSpacesDto{" + "id=" + id.get() + ", nombre=" + nombre.get() + ", capacidad=" + capacidad.get() + ", estado=" + estado.get() + '}';
    }
}
