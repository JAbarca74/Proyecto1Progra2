// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// 2) DTO: UsuariosDto.java
// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
package cr.ac.una.proyecto1progra2.model;

import javafx.beans.property.*;

public class UsuariosDto {

    private final StringProperty id         = new SimpleStringProperty();
    private final StringProperty username   = new SimpleStringProperty();
    private final StringProperty nombre     = new SimpleStringProperty();
    private final StringProperty apellido   = new SimpleStringProperty();
    private final StringProperty correo     = new SimpleStringProperty();
    private final StringProperty contraseña = new SimpleStringProperty();
    private final StringProperty rolId      = new SimpleStringProperty();
    private final BooleanProperty estado    = new SimpleBooleanProperty();
    private Long version;

    public UsuariosDto() {}

    public UsuariosDto(Usuarios u) {
        setId(u.getId());
        setUsername(u.getUsername());
        setContraseña(u.getPassword());
        setRolId(u.getRoleId() != null ? u.getRoleId().longValue() : null);
        setEstado(u.getIsActive());
        setNombre(u.getNombre());
        setApellido(u.getApellido());
        setCorreo(u.getCorreoElectronico());
        this.version = u.getVersion();
    }

    // ID
    public Long getId() {
        String v = id.get();
        return (v != null && !v.isEmpty()) ? Long.valueOf(v) : null;
    }
    public void setId(Long v) {
        id.set(v != null ? v.toString() : null);
    }

    // USERNAME
    public String getUsername() { return username.get(); }
    public void setUsername(String u) { username.set(u); }

    // CONTRASEÑA
    public String getContraseña() { return contraseña.get(); }
    public void setContraseña(String p) { contraseña.set(p); }

    // ROL_ID
    public Long getRolId() {
        String v = rolId.get();
        return (v != null && !v.isEmpty()) ? Long.valueOf(v) : null;
    }
    public void setRolId(Long r) {
        rolId.set(r != null ? r.toString() : null);
    }

    // ESTADO como boolean
    public Boolean getEstado() { return estado.get(); }
    public void setEstado(Boolean b) { estado.set(b); }
    /** setter desde String 'A'/'I' */
    public void setEstado(String s) {
        estado.set("A".equalsIgnoreCase(s));
    }

    // NOMBRE
    public String getNombre() { return nombre.get(); }
    public void setNombre(String n) { nombre.set(n); }

    // APELLIDO
    public String getApellido() { return apellido.get(); }
    public void setApellido(String a) { apellido.set(a); }

    // CORREO
    public String getCorreo() { return correo.get(); }
    public void setCorreo(String c) { correo.set(c); }

    // VERSION
    public Long getVersion() { return version; }
    public void setVersion(Long v) { version = v; }
}
