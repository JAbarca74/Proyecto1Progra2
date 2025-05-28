package cr.ac.una.proyecto1progra2.DTO;

import cr.ac.una.proyecto1progra2.model.Spaces;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class SpacesDto {
    private final SimpleLongProperty id = new SimpleLongProperty();
    private final SimpleStringProperty nombre = new SimpleStringProperty();
    private Integer capacidad;
    private boolean modificado = false;

    // Posicionamiento
    private Integer rowIndex = 0;
    private Integer columnIndex = 0;
    private Integer rowSpan = 1;
    private Integer colSpan = 1;

    public SpacesDto() {}

    public SpacesDto(Spaces space) {
        setId(space.getId());
        setNombre(space.getName());
        setCapacidad(space.getCapacity() != null ? space.getCapacity().intValue() : null);
        setRowIndex(space.getRowIndex() != null ? space.getRowIndex() : 0);
        setColumnIndex(space.getColumnIndex() != null ? space.getColumnIndex() : 0);
        setRowSpan(space.getRowSpan() != null ? space.getRowSpan() : 1);
        setColSpan(space.getColSpan() != null ? space.getColSpan() : 1);
    }

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

    public Integer getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

    public Integer getColumnIndex() {
        return columnIndex;
    }

    public void setColumnIndex(Integer columnIndex) {
        this.columnIndex = columnIndex;
    }

    public Integer getRowSpan() {
        return rowSpan != null ? rowSpan : 1;
    }

    public void setRowSpan(Integer rowSpan) {
        this.rowSpan = rowSpan;
    }

    public Integer getColSpan() {
        return colSpan != null ? colSpan : 1;
    }

    public void setColSpan(Integer colSpan) {
        this.colSpan = colSpan;
    }

    // Métodos auxiliares para integración visual
    public Integer getRow() {
        return getRowIndex();
    }

    public void setRow(Integer row) {
        setRowIndex(row);
    }

    public Integer getColumn() {
        return getColumnIndex();
    }

    public void setColumn(Integer column) {
        setColumnIndex(column);
    }
}