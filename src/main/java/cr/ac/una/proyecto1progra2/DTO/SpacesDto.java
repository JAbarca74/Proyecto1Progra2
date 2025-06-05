package cr.ac.una.proyecto1progra2.DTO;

import cr.ac.una.proyecto1progra2.model.CoworkingSpaces;
import cr.ac.una.proyecto1progra2.model.Spaces;

public class SpacesDto {
    private Long id;
    private String nombre;
    private Integer capacidad;
    private Integer rowIndex;
    private Integer columnIndex;
    private Integer rowSpan;
    private Integer colSpan;
    private CoworkingSpaces coworkingSpace;

    public SpacesDto() {
    }

    public SpacesDto(Spaces space) {
        this.id = space.getId();
        this.nombre = space.getName();
        this.capacidad = space.getCapacity();
        this.rowIndex = space.getRowIndex();
        this.columnIndex = space.getColumnIndex();
        this.rowSpan = space.getRowSpan();
        this.colSpan = space.getColSpan();
        this.coworkingSpace = space.getCoworkingSpace(); // asignar si está presente
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(Integer capacidad) {
        this.capacidad = capacidad;
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
        return rowSpan;
    }

    public void setRowSpan(Integer rowSpan) {
        this.rowSpan = rowSpan;
    }

    public Integer getColSpan() {
        return colSpan;
    }

    public void setColSpan(Integer colSpan) {
        this.colSpan = colSpan;
    }

    public CoworkingSpaces getCoworkingSpace() {
        return coworkingSpace;
    }

    public void setCoworkingSpace(CoworkingSpaces coworkingSpace) {
        this.coworkingSpace = coworkingSpace;
    }

    // Atajos para GridPane
    public void setRow(int row) {
        this.rowIndex = row;
    }

    public void setColumn(int col) {
        this.columnIndex = col;
    }

    public int getRow() {
        return this.rowIndex != null ? this.rowIndex : 0;
    }

    public int getColumn() {
        return this.columnIndex != null ? this.columnIndex : 0;
    }

    @Override
    public String toString() {
        return "SpacesDto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", capacidad=" + capacidad +
                ", rowIndex=" + rowIndex +
                ", columnIndex=" + columnIndex +
                ", rowSpan=" + rowSpan +
                ", colSpan=" + colSpan +
                '}';
    }
}