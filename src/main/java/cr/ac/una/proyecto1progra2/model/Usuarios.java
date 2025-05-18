package cr.ac.una.proyecto1progra2.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Version;


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
    private String isActive; // Guardaremos como "A" o "I"

    @Version
    @Basic(optional = false)
    @Column(name = "VERSION")
    private Long version;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId", fetch = FetchType.LAZY)
    private Collection<Reservations> reservationsCollection;

    public Usuarios() {
    }

    public Usuarios(Long id) {
        this.id = id;
    }

    public Usuarios(UsuariosDto usuariosDto) {
    this();
    actualizar(usuariosDto); // No asignar ID aquí
}


    public void actualizar(UsuariosDto usuariosDto) {
        this.username = usuariosDto.getNombre();
        this.password = usuariosDto.getContraseña();
        this.roleId = usuariosDto.getRolId() != null ? BigInteger.valueOf(usuariosDto.getRolId()) : null;
        this.isActive = usuariosDto.getEstado();
        this.version = usuariosDto.getVersion();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigInteger getRoleId() {
        return roleId;
    }

    public void setRoleId(BigInteger roleId) {
        this.roleId = roleId;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Collection<Reservations> getReservationsCollection() {
        return reservationsCollection;
    }

    public void setReservationsCollection(Collection<Reservations> reservationsCollection) {
        this.reservationsCollection = reservationsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Usuarios)) {
            return false;
        }
        Usuarios other = (Usuarios) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    @Override
    public String toString() {
        return "cr.ac.una.proyecto1progra2.model.Usuarios[ id=" + id + " ]";
    }
}
