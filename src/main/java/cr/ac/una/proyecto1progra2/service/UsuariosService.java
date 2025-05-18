package cr.ac.una.proyecto1progra2.service;

import cr.ac.una.proyecto1progra2.model.Usuarios;
import cr.ac.una.proyecto1progra2.model.UsuariosDto;
import cr.ac.una.proyecto1progra2.util.EntityManagerHelper;
import cr.ac.una.proyecto1progra2.util.Respuesta;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class UsuariosService {

    private final EntityManager em = EntityManagerHelper.getInstance().getManager();
    private EntityTransaction et;

    public Respuesta getUsuario(String username, String password) {
        try {
            // 1) Buscar por nombre (mayúsculas)
            Query qry = em.createNamedQuery("Usuarios.findByUsername", Usuarios.class);
            qry.setParameter("username", username.toUpperCase());
            Usuarios usuario = (Usuarios) qry.getSingleResult();

            // 2) Validar contraseña
            if (!usuario.getPassword().equals(password)) {
                return new Respuesta(false,
                    "Credenciales inválidas.",
                    "getUsuario InvalidCredentials");
            }

            // 3) Verificar estado “A”ctivo
            if (!"A".equalsIgnoreCase(usuario.getIsActive())) {
                return new Respuesta(false,
                    "La cuenta está deshabilitada.",
                    "getUsuario InactiveUser");
            }

            // Todo ok: construir DTO y devolver
            UsuariosDto dto = new UsuariosDto(usuario);
            return new Respuesta(true, "", "", "Usuario", dto);

        } catch (NoResultException ex) {
            return new Respuesta(false,
                "No existe un usuario con ese nombre.",
                "getUsuario NoResultException");
        } catch (Exception ex) {
            Logger.getLogger(UsuariosService.class.getName())
                  .log(Level.SEVERE, "Error obteniendo el usuario [" + username + "]", ex);
            return new Respuesta(false,
                "Error interno al iniciar sesión.",
                "getUsuario " + ex.getMessage());
        }
    }

    public Respuesta getUsuario(Long id) {
        try {
            Query qry = em.createNamedQuery("Usuarios.findById", Usuarios.class);
            qry.setParameter("id", id);
            UsuariosDto dto = new UsuariosDto((Usuarios) qry.getSingleResult());
            return new Respuesta(true, "", "", "Usuario", dto);
        } catch (NoResultException ex) {
            return new Respuesta(false,
                "No existe un usuario con el ID ingresado.",
                "getUsuario NoResultException");
        } catch (Exception ex) {
            Logger.getLogger(UsuariosService.class.getName())
                  .log(Level.SEVERE, "Error obteniendo el usuario [" + id + "]", ex);
            return new Respuesta(false,
                "Error interno obteniendo el usuario.",
                "getUsuario " + ex.getMessage());
        }
    }

    public Respuesta guardarUsuario(UsuariosDto usuarioDto) {
        try {
            et = em.getTransaction();
            et.begin();
            Usuarios usuario;
            if (usuarioDto.getId() != null && usuarioDto.getId() > 0) {
                usuario = em.find(Usuarios.class, usuarioDto.getId());
                if (usuario == null) {
                    et.rollback();
                    return new Respuesta(false,
                        "No se encontró el usuario a modificar.",
                        "guardarUsuario NoResultException");
                }
                usuario.actualizar(usuarioDto);
                usuario = em.merge(usuario);
            } else {
                usuario = new Usuarios(usuarioDto);
                em.persist(usuario);
            }
            et.commit();
            return new Respuesta(true, "", "", "Usuario", new UsuariosDto(usuario));
        } catch (Exception ex) {
            if (et != null && et.isActive()) et.rollback();
            Logger.getLogger(UsuariosService.class.getName())
                  .log(Level.SEVERE, "Ocurrió un error al guardar el usuario.", ex);
            return new Respuesta(false,
                "Error interno al guardar el usuario.",
                "guardarUsuario " + ex.getMessage());
        }
    }

    public Respuesta eliminarUsuario(Long id) {
        try {
            et = em.getTransaction();
            et.begin();
            if (id == null || id <= 0) {
                et.rollback();
                return new Respuesta(false,
                    "Debe indicar un usuario válido para eliminar.",
                    "eliminarUsuario InvalidId");
            }
            Usuarios usuario = em.find(Usuarios.class, id);
            if (usuario == null) {
                et.rollback();
                return new Respuesta(false,
                    "No se encontró el usuario a eliminar.",
                    "eliminarUsuario NoResultException");
            }
            em.remove(usuario);
            et.commit();
            return new Respuesta(true, "", "");
        } catch (Exception ex) {
            if (et != null && et.isActive()) et.rollback();
            if (ex.getCause() != null
             && ex.getCause().getCause() instanceof SQLIntegrityConstraintViolationException) {
                return new Respuesta(false,
                    "No se puede eliminar el usuario porque tiene registros dependientes.",
                    "eliminarUsuario ConstraintViolation");
            }
            Logger.getLogger(UsuariosService.class.getName())
                  .log(Level.SEVERE, "Error eliminando el usuario.", ex);
            return new Respuesta(false,
                "Error interno al eliminar el usuario.",
                "eliminarUsuario " + ex.getMessage());
        }
    }

    public Respuesta listarUsuarios() {
        try {
            Query qry = em.createNamedQuery("Usuarios.findAll", Usuarios.class);
            List<Usuarios> list = qry.getResultList();
            List<UsuariosDto> dtoList = new ArrayList<>();
            for (Usuarios u : list) {
                dtoList.add(new UsuariosDto(u));
            }
            return new Respuesta(true, "", "", "Usuarios", dtoList);
        } catch (Exception ex) {
            Logger.getLogger(UsuariosService.class.getName())
                  .log(Level.SEVERE, "Error listando usuarios.", ex);
            return new Respuesta(false,
                "Error interno listando usuarios.",
                "listarUsuarios " + ex.getMessage());
        }
    }
}
