<<<<<<< Updated upstream
<<<<<<< Updated upstream:src/main/java/cr/ac/una/proyecto1progra2/model/UsuariosDto.java
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
=======
=======
>>>>>>> Stashed changes
package cr.ac.una.proyecto1progra2.DTO;

import cr.ac.una.proyecto1progra2.model.Usuarios;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class UsuariosDto {

    public final SimpleStringProperty id;
    public final SimpleStringProperty nombre;
    public final SimpleStringProperty apellido;
    public final SimpleStringProperty correo;
    public final SimpleStringProperty contraseña;
    public final SimpleStringProperty rolId;
    public final SimpleBooleanProperty estado;
    public final SimpleStringProperty username;

<<<<<<< Updated upstream
>>>>>>> Stashed changes:src/main/java/cr/ac/una/proyecto1progra2/DTO/UsuariosDto.java
    private Long version;

<<<<<<< Updated upstream:src/main/java/cr/ac/una/proyecto1progra2/model/UsuariosDto.java
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
=======
=======
    private Long version;
    private Boolean modificado;

>>>>>>> Stashed changes
    public UsuariosDto() {
        this.modificado = false;
        this.id = new SimpleStringProperty();
        this.nombre = new SimpleStringProperty();
        this.apellido = new SimpleStringProperty();
        this.correo = new SimpleStringProperty();
        this.contraseña = new SimpleStringProperty();
        this.rolId = new SimpleStringProperty();
        this.estado = new SimpleBooleanProperty(true);
        this.username = new SimpleStringProperty();
<<<<<<< Updated upstream
>>>>>>> Stashed changes:src/main/java/cr/ac/una/proyecto1progra2/DTO/UsuariosDto.java
    }

    // ID
=======
    }

    /** Constructor desde la entidad Usuarios */
  public UsuariosDto(Usuarios usuario) {
    this();
    this.id.set(usuario.getId() != null ? usuario.getId().toString() : null);
    this.nombre.set(usuario.getNombre());
    this.apellido.set(usuario.getApellido());
    this.correo.set(usuario.getCorreoElectronico());
    this.contraseña.set(usuario.getPassword());
    this.rolId.set(usuario.getRoleId() != null ? usuario.getRoleId().toString() : null);
    this.estado.set(usuario.getIsActive() != null && usuario.getIsActive().equals("A"));
    this.username.set(usuario.getUsername()); // ✅ Agrega esta línea
    this.version = usuario.getVersion();
}

>>>>>>> Stashed changes
    public Long getId() {
        String v = id.get();
        return (v != null && !v.isEmpty()) ? Long.valueOf(v) : null;
    }
<<<<<<< Updated upstream
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
=======

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

>>>>>>> Stashed changes
    public Long getRolId() {
        String v = rolId.get();
        return (v != null && !v.isEmpty()) ? Long.valueOf(v) : null;
    }
<<<<<<< Updated upstream
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
=======

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
>>>>>>> Stashed changes
}
