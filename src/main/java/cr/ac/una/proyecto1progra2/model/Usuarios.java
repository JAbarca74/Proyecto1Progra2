// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// 1) Entidad JPA: Usuarios.java
// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
package cr.ac.una.proyecto1progra2.model;

import cr.ac.una.proyecto1progra2.DTO.UsuariosDto;
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
                query = "SELECT u FROM Usuarios u WHERE UPPER(u.username) = :username"),
    @NamedQuery(name = "Usuarios.findByCredentials",
                query = "SELECT u FROM Usuarios u WHERE u.username = :username AND u.password = :password")
})
public class Usuarios implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuarios_seq_gen")
    @SequenceGenerator(name = "usuarios_seq_gen", sequenceName = "USUARIOS_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "USERNAME", nullable = false, unique = true)
    private String username;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "ROLE_ID", nullable = false)
    private BigInteger roleId;

    @Column(name = "IS_ACTIVE", nullable = false, length = 1)
    private String isActive;  // 'A' / 'I'

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "APELLIDO")
    private String apellido;

    @Column(name = "CORREO_ELECTRONICO")
    private String correoElectronico;

    @Version
    @Column(name = "VERSION", nullable = false)
    private Long version;

    @OneToMany(mappedBy = "userId", fetch = FetchType.LAZY)
    private Collection<Reservations> reservationsCollection;

    public Usuarios() {}

    /** Copia todos los datos desde el DTO */
    public void actualizar(UsuariosDto dto) {
        this.username          = dto.getUsername();
        this.password          = dto.getContraseña();
        this.roleId            = dto.getRolId() != null
                                 ? BigInteger.valueOf(dto.getRolId())
                                 : null;
        this.isActive          = dto.getEstado() ? "A" : "I";
        this.nombre            = dto.getNombre();
        this.apellido          = dto.getApellido();
        this.correoElectronico = dto.getCorreo();
        this.version           = dto.getVersion();
    }

    // getters & setters...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String u) { username = u; }

    public String getPassword() { return password; }
    public void setPassword(String p) { password = p; }

    public BigInteger getRoleId() { return roleId; }
    public void setRoleId(BigInteger r) { roleId = r; }

    /** true si IS_ACTIVE = 'A' */
    public boolean getIsActive() { return "A".equalsIgnoreCase(isActive); }
    public void setIsActive(String a) { isActive = a; }

    public String getNombre() { return nombre; }
    public void setNombre(String n) { nombre = n; }

    public String getApellido() { return apellido; }
    public void setApellido(String a) { apellido = a; }

    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String c) { correoElectronico = c; }

    public Long getVersion() { return version; }
    public void setVersion(Long v) { version = v; }

    public Collection<Reservations> getReservationsCollection() {
        return reservationsCollection;
    }
    public void setReservationsCollection(Collection<Reservations> c) {
        reservationsCollection = c;
    }
}
