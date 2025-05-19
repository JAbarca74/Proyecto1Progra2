package cr.ac.una.proyecto1progra2.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class UsuariosDto {

    public final SimpleStringProperty id;
    public final SimpleStringProperty nombre;
    public final SimpleStringProperty apellido; // ✅ NUEVO
    public final SimpleStringProperty correo;
    public final SimpleStringProperty contraseña;
    public final SimpleStringProperty rolId;
    public final SimpleBooleanProperty estado;
    public final SimpleStringProperty username;

    private Long version;
    private Boolean modificado;

    public UsuariosDto() {
        this.modificado = false;
        this.id = new SimpleStringProperty();
        this.nombre = new SimpleStringProperty();
        this.apellido = new SimpleStringProperty(); // ✅ NUEVO
        this.correo = new SimpleStringProperty();
        this.contraseña = new SimpleStringProperty();
        this.rolId = new SimpleStringProperty();
        this.estado = new SimpleBooleanProperty(true);
        this.username = new SimpleStringProperty();
    }

    /** Constructor desde la entidad Usuarios */
    public UsuariosDto(Usuarios usuario) {
        this();
        this.id.set(usuario.getId() != null ? usuario.getId().toString() : null);
        this.nombre.set(usuario.getNombre());
        this.apellido.set(usuario.getApellido()); // ✅ NUEVO
        this.correo.set(usuario.getCorreoElectronico());
        this.contraseña.set(usuario.getPassword());
        this.rolId.set(usuario.getRoleId() != null ? usuario.getRoleId().toString() : null);
        this.estado.set(usuario.getIsActive() != null && usuario.getIsActive().equals("A"));
        this.version = usuario.getVersion();
    }

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
    
    public String getUsername() {
    return username.get();
}
public void setUsername(String username) {
    this.username.set(username);
}


    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public String getApellido() {
        return apellido.get();
    }

    public void setApellido(String apellido) {
        this.apellido.set(apellido);
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

    public Boolean getIsActive() {
        return estado.get();
    }

    @Override
    public String toString() {
        return String.format(
            "UsuariosDto{id=%s, nombre=%s, apellido=%s, correo=%s, rolId=%s, estado=%s}",
            id.get(), nombre.get(), apellido.get(), correo.get(), rolId.get(), estado.get()
        );
    }
}
