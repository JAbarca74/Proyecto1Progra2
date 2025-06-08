package cr.ac.una.proyecto1progra2.model;

import cr.ac.una.proyecto1progra2.DTO.SpacesDto;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

@Entity
@Table(name = "TB_SPACES")
@NamedQueries({
    @NamedQuery(name = "Spaces.findAll", query = "SELECT s FROM Spaces s"),
    @NamedQuery(name = "Spaces.findById", query = "SELECT s FROM Spaces s WHERE s.id = :id"),
    @NamedQuery(name = "Spaces.findByName", query = "SELECT s FROM Spaces s WHERE UPPER(s.name) = :name")
})
public class Spaces implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "spaces_seq")
    @SequenceGenerator(name = "spaces_seq", sequenceName = "SPACES_SEQ", allocationSize = 1)
    private Long id;

    @Basic(optional = false)
    @Column(name = "NAME")
    private String name;

    @Column(name = "CAPACITY")
    private Integer capacity;

    @Column(name = "ROW_INDEX")
    private Integer rowIndex;

    @Column(name = "COLUMN_INDEX")
    private Integer columnIndex;

    @Column(name = "ROW_SPAN")
    private Integer rowSpan;

    @Column(name = "COL_SPAN")
    private Integer colSpan;

    // Relación con CoworkingSpaces (colección original)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "spaceId", fetch = FetchType.LAZY)
    private Collection<CoworkingSpaces> coworkingSpacesCollection;

    // Nueva relación directa con un CoworkingSpaces
    @OneToOne(mappedBy = "spaceId", fetch = FetchType.LAZY)
    private CoworkingSpaces coworkingSpace;

    public Spaces() {
    }

    public Spaces(Long id) {
        this.id = id;
    }

    public Spaces(SpacesDto dto) {
        this();
        actualizar(dto);
    }

 public void actualizar(SpacesDto dto) {
    this.name = dto.getNombre();
    this.capacity = dto.getCapacidad();
    this.rowIndex = dto.getRowIndex();
    this.columnIndex = dto.getColumnIndex();
    this.rowSpan = dto.getRowSpan();
    this.colSpan = dto.getColSpan();
}

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
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

    public Collection<CoworkingSpaces> getCoworkingSpacesCollection() {
        return coworkingSpacesCollection;
    }

    public void setCoworkingSpacesCollection(Collection<CoworkingSpaces> coworkingSpacesCollection) {
        this.coworkingSpacesCollection = coworkingSpacesCollection;
    }

    public CoworkingSpaces getCoworkingSpace() {
        return coworkingSpace;
    }

    public void setCoworkingSpace(CoworkingSpaces coworkingSpace) {
        this.coworkingSpace = coworkingSpace;
    }

    @Override
    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Spaces)) return false;
        Spaces other = (Spaces) object;
        return (this.id != null || other.id == null) &&
               (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "Spaces[ id=" + id + " ]";
    }
}