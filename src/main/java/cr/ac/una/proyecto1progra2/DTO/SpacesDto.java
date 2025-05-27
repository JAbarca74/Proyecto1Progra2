package cr.ac.una.proyecto1progra2.DTO;

import cr.ac.una.proyecto1progra2.model.Spaces;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class SpacesDto {
    private final SimpleLongProperty id = new SimpleLongProperty();
    private final SimpleStringProperty nombre = new SimpleStringProperty();
    private Integer capacidad;
    private boolean modificado = false;

    public SpacesDto() {}

    /** Constructor que mapea desde la entidad Spaces */
    public SpacesDto(Spaces space) {
        setId(space.getId());
        setNombre(space.getName());
        setCapacidad(space.getCapacity() != null ? space.getCapacity().intValue() : null);
        // No se debe llamar a getVersion() porque ya no existe
    }

    // getters y setters

    public Long getId() {
        return id.get();
    }

    public void setId(Long id) {
        this.id.set(id != null ? id : 0L);
    }

    public SimpleLongProperty idProperty() {
        return id;
    }

    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public SimpleStringProperty nombreProperty() {
        return nombre;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
    }

    public boolean isModificado() {
        return modificado;
    }

    public void setModificado(boolean modificado) {
        this.modificado = modificado;
    }
}