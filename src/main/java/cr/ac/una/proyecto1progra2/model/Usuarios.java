package cr.ac.una.proyecto1progra2.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.*;


@Entity
@Table(name = "TB_USUARIOS")
@NamedQueries({
  @NamedQuery(name = "Usuarios.findAll",
              query = "SELECT u FROM Usuarios u"),
  @NamedQuery(name = "Usuarios.findById",
              query = "SELECT u FROM Usuarios u WHERE u.id = :id"),
  @NamedQuery(name = "Usuarios.findByUsername",
              query = "SELECT u FROM Usuarios u WHERE UPPER(u.username) = :username",
              hints = @QueryHint(name = "eclipselink.refresh", value = "true")),
  @NamedQuery(name = "Usuarios.findByCredentials",
              query = "SELECT u FROM Usuarios u WHERE UPPER(u.username) = :username AND u.password = :password",
              hints = @QueryHint(name = "eclipselink.refresh", value = "true"))
})

public class Usuarios implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;

    @Basic(optional = false)
    @Column(name = "USERNAME")
    private String username;

    @Basic(optional = false)
    @Column(name = "PASSWORD")
    private String password;

    @Basic(optional = false)
    @Column(name = "ROLE_ID")
    private BigInteger roleId;

    @Basic(optional = false)
    @Column(name = "IS_ACTIVE")
    private String isActive;

    @Version
    @Basic(optional = false)
    @Column(name = "VERSION")
    private Long version;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "APELLIDO")
    private String apellido;

    @Column(name = "CORREO_ELECTRONICO")
    private String correoElectronico;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId", fetch = FetchType.LAZY)
    private Collection<Reservations> reservationsCollection;

    public Usuarios() {
    }

    public Usuarios(Long id) {
        this.id = id;
    }

    public Usuarios(UsuariosDto usuariosDto) {
        this();
        actualizar(usuariosDto);
    }

    public void actualizar(UsuariosDto usuariosDto) {
        this.username = usuariosDto.getNombre(); // Si tienes username por separado, ajusta aquí
        this.nombre = usuariosDto.getNombre();
        this.apellido = usuariosDto.getApellido();
        this.correoElectronico = usuariosDto.getCorreo();
        this.password = usuariosDto.getContraseña();
        this.roleId = usuariosDto.getRolId() != null ? BigInteger.valueOf(usuariosDto.getRolId()) : null;
        this.isActive = usuariosDto.getEstado();
        this.version = usuariosDto.getVersion();
    }

    // Getters y Setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public BigInteger getRoleId() { return roleId; }
    public void setRoleId(BigInteger roleId) { this.roleId = roleId; }

    public String getIsActive() { return isActive; }
    public void setIsActive(String isActive) { this.isActive = isActive; }

    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }

    public Collection<Reservations> getReservationsCollection() { return reservationsCollection; }
    public void setReservationsCollection(Collection<Reservations> reservationsCollection) { this.reservationsCollection = reservationsCollection; }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Usuarios)) return false;
        Usuarios other = (Usuarios) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "cr.ac.una.proyecto1progra2.model.Usuarios[ id=" + id + " ]";
    }
}
