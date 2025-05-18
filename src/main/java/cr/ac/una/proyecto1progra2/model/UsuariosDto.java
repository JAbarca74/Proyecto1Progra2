package cr.ac.una.proyecto1progra2.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class UsuariosDto {

    public final SimpleStringProperty id;
    public final SimpleStringProperty nombre;
    public final SimpleStringProperty correo;
    public final SimpleStringProperty contraseña;
    public final SimpleStringProperty rolId;
    public final SimpleBooleanProperty estado;
    private Long version;
    private Boolean modificado;

    public UsuariosDto() {
        this.modificado = false;
        this.id         = new SimpleStringProperty();
        this.nombre     = new SimpleStringProperty();
        this.correo     = new SimpleStringProperty();
        this.contraseña = new SimpleStringProperty();
        this.rolId      = new SimpleStringProperty();
        this.estado     = new SimpleBooleanProperty(true);
    }

    /** Constructor a partir de la entidad Usuarios */
    public UsuariosDto(Usuarios usuario) {
        this();
        this.id.set(usuario.getId() != null
            ? usuario.getId().toString() : null);
        this.nombre.set(usuario.getUsername());
        // ← Aquí cambiamos getEmail() por getUsername()
        this.correo.set(usuario.getUsername());
        this.contraseña.set(usuario.getPassword());
        this.rolId.set(usuario.getRoleId() != null
            ? usuario.getRoleId().toString() : null);
        this.estado.set(usuario.getIsActive() != null
            && usuario.getIsActive().equals('S'));
        this.version = usuario.getVersion();
    }

    // getters y setters que usan las propiedades internas para TableView y lógica
    public Long getId() {
        String v = id.get();
        return (v != null && !v.isEmpty()) ? Long.valueOf(v) : null;
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

    public String getCorreo() {
        return correo.get();
    }
    public void setCorreo(String correo) {
        this.correo.set(correo);
    }

    public String getContraseña() {
        return contraseña.get();
    }
    public void setContraseña(String contraseña) {
        this.contraseña.set(contraseña);
    }

    public Long getRolId() {
        String v = rolId.get();
        return (v != null && !v.isEmpty()) ? Long.valueOf(v) : null;
    }
    public void setRolId(Long rolId) {
        this.rolId.set(rolId != null ? rolId.toString() : null);
    }

    /** Para la columna “Activo” en la tabla mostramos “A” o “I” */
    public String getEstado() {
        return estado.get() ? "A" : "I";
    }
    public void setEstado(String estado) {
        this.estado.set("A".equalsIgnoreCase(estado));
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
        return String.format(
            "UsuariosDto{id=%s, nombre=%s, correo=%s, rolId=%s, estado=%s}",
            id.get(), nombre.get(), correo.get(),
            rolId.get(), estado.get()
        );
    }
}
