package cr.ac.una.proyecto1progra2.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class UsuariosDto {

    public SimpleStringProperty id;
    public SimpleStringProperty nombre;
    public SimpleStringProperty correo;
    public SimpleStringProperty contraseña;
    public SimpleStringProperty rolId;
    public SimpleBooleanProperty estado;
    public Long version;
    private Boolean modificado;

    public UsuariosDto() {
        this.modificado = false;
        this.id = new SimpleStringProperty();
        this.nombre = new SimpleStringProperty();
        this.correo = new SimpleStringProperty();
        this.contraseña = new SimpleStringProperty();
        this.rolId = new SimpleStringProperty();
        this.estado = new SimpleBooleanProperty(true);
    }

    // 🚀 Constructor que recibe la entidad Usuarios (nuevo)
    public UsuariosDto(Usuarios usuario) {
        this();
        this.id.set(usuario.getId() != null ? usuario.getId().toString() : null);
        this.nombre.set(usuario.getUsername());
        this.contraseña.set(usuario.getPassword());
        this.rolId.set(usuario.getRoleId() != null ? usuario.getRoleId().toString() : null);
        this.estado.set(usuario.getIsActive() != null && usuario.getIsActive().equals('S'));
        this.version = usuario.getVersion();
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
        if (rolId.get() != null && !rolId.get().isEmpty())
            return Long.valueOf(rolId.get());
        else
            return null;
    }

    public void setRolId(Long rolId) {
        this.rolId.set(rolId != null ? rolId.toString() : null);
    }

    public String getEstado() {
        return estado.getValue() ? "A" : "I";
    }

    public void setEstado(String estado) {
        this.estado.setValue(estado.equalsIgnoreCase("A"));
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
        return "UsuariosDto{" + 
                "id=" + id.get() + 
                ", nombre=" + nombre.get() + 
                ", correo=" + correo.get() + 
                ", contraseña=" + contraseña.get() + 
                ", rolId=" + rolId.get() + 
                ", estado=" + estado.get() + '}';
    }

   public Boolean getIsActive() {
    return estado.get();
}



}
