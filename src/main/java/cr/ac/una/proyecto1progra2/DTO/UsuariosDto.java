package cr.ac.una.proyecto1progra2.DTO;

import cr.ac.una.proyecto1progra2.model.Usuarios;
import javafx.beans.property.*;

public class UsuariosDto {

    private final StringProperty id         = new SimpleStringProperty();
    private final StringProperty username   = new SimpleStringProperty();
    private final StringProperty nombre     = new SimpleStringProperty();
    private final StringProperty apellido   = new SimpleStringProperty();
    private final StringProperty correo     = new SimpleStringProperty();
    private final StringProperty contraseña = new SimpleStringProperty();
    private final StringProperty rolId      = new SimpleStringProperty();
    private final BooleanProperty estado    = new SimpleBooleanProperty(true);
    private Long version;
    private Boolean modificado = false;

    public UsuariosDto() {}

    public UsuariosDto(Usuarios usuario) {
        this();
        setId(usuario.getId());
        setUsername(usuario.getUsername());
        setNombre(usuario.getNombre());
        setApellido(usuario.getApellido());
        setCorreo(usuario.getCorreoElectronico());
        setContraseña(usuario.getPassword());
        setRolId(usuario.getRoleId() != null ? usuario.getRoleId().longValue() : null);
        setEstado(usuario.getIsActive());
        setVersion(usuario.getVersion());
    }

    public Long getId() {
        String v = id.get();
        return (v != null && !v.isEmpty()) ? Long.valueOf(v) : null;
    }

    public void setId(Long v) {
        id.set(v != null ? v.toString() : null);
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getNombre() {
        return nombre.get();
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

    public Boolean getEstado() {
        return estado.get();
    }

    public void setEstado(Boolean b) {
        estado.set(b);
    }

    public void setEstado(String s) {
        estado.set("A".equalsIgnoreCase(s));
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Boolean getModificado() {
        return modificado;
    }

    public void setModificado(Boolean modificado) {
        this.modificado = modificado;
    }

    @Override
    public String toString() {
        return String.format(
            "UsuariosDto{id=%s, nombre=%s, apellido=%s, correo=%s, rolId=%s, estado=%s}",
            id.get(), nombre.get(), apellido.get(), correo.get(), rolId.get(), estado.get()
        );
    }
} 
