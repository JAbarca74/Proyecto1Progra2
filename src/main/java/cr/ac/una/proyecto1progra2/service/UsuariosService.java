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
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

public class UsuariosService {

    EntityManager em = EntityManagerHelper.getInstance().getManager();
    private EntityTransaction et;

    public Respuesta getUsuario(String username, String password) {
        try {
            Query qryUsuario = em.createNamedQuery("Usuarios.findByCredentials", Usuarios.class);
            qryUsuario.setParameter("username", username.toUpperCase());
            qryUsuario.setParameter("password", password);
            UsuariosDto usuarioDto = new UsuariosDto((Usuarios) qryUsuario.getSingleResult());
            return new Respuesta(true, "", "", "Usuario", usuarioDto);
        } catch (NoResultException ex) {
            return new Respuesta(false, "No existe un usuario con las credenciales ingresadas.", "getUsuario NoResultException");
        } catch (NonUniqueResultException ex) {
            Logger.getLogger(UsuariosService.class.getName()).log(Level.SEVERE, "Ocurrió un error al consultar el usuario.", ex);
            return new Respuesta(false, "Ocurrió un error al consultar el usuario.", "getUsuario NonUniqueResultException");
        } catch (Exception ex) {
            Logger.getLogger(UsuariosService.class.getName()).log(Level.SEVERE, "Error obteniendo el usuario [" + username + "]", ex);
            return new Respuesta(false, "Error obteniendo el usuario.", "getUsuario " + ex.getMessage());
        }
    }

    public Respuesta getUsuario(Long id) {
        try {
            Query qryUsuario = em.createNamedQuery("Usuarios.findById", Usuarios.class);
            qryUsuario.setParameter("id", id);
            UsuariosDto usuarioDto = new UsuariosDto((Usuarios) qryUsuario.getSingleResult());
            return new Respuesta(true, "", "", "Usuario", usuarioDto);
        } catch (NoResultException ex) {
            return new Respuesta(false, "No existe un usuario con el id ingresado.", "getUsuario NoResultException");
        } catch (Exception ex) {
            Logger.getLogger(UsuariosService.class.getName()).log(Level.SEVERE, "Error obteniendo el usuario [" + id + "]", ex);
            return new Respuesta(false, "Error obteniendo el usuario.", "getUsuario " + ex.getMessage());
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
                    return new Respuesta(false, "No se encontró el usuario a modificar.", "guardarUsuario NoResultException");
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
            et.rollback();
            Logger.getLogger(UsuariosService.class.getName()).log(Level.SEVERE, "Ocurrió un error al guardar el usuario.", ex);
            return new Respuesta(false, "Ocurrió un error al guardar el usuario.", "guardarUsuario " + ex.getMessage());
        }
    }

    public Respuesta eliminarUsuario(Long id) {
        try {
            et = em.getTransaction();
            et.begin();
            Usuarios usuario;
            if (id != null && id > 0) {
                usuario = em.find(Usuarios.class, id);
                if (usuario == null) {
                    et.rollback();
                    return new Respuesta(false, "No se encontró el usuario a eliminar.", "eliminarUsuario NoResultException");
                }
                em.remove(usuario);
            } else {
                et.rollback();
                return new Respuesta(false, "Debe cargar el usuario a eliminar.", "eliminarUsuario NoResultException");
            }
            et.commit();
            return new Respuesta(true, "", "");
        } catch (Exception ex) {
            et.rollback();
            if (ex.getCause() != null && ex.getCause().getCause() instanceof SQLIntegrityConstraintViolationException) {
                return new Respuesta(false, "No se puede eliminar el usuario porque tiene relaciones con otros registros.", "eliminarUsuario " + ex.getMessage());
            }
            Logger.getLogger(UsuariosService.class.getName()).log(Level.SEVERE, "Error eliminando el usuario.", ex);
            return new Respuesta(false, "Error eliminando el usuario.", "eliminarUsuario " + ex.getMessage());
        }
    }

    public Respuesta listarUsuarios() {
        try {
            Query qry = em.createNamedQuery("Usuarios.findAll", Usuarios.class);
            List<Usuarios> usuarios = qry.getResultList();
            List<UsuariosDto> usuariosDto = new ArrayList<>();
            for (Usuarios usuario : usuarios) {
                usuariosDto.add(new UsuariosDto(usuario));
            }
            return new Respuesta(true, "", "", "Usuarios", usuariosDto);
        } catch (Exception ex) {
            Logger.getLogger(UsuariosService.class.getName()).log(Level.SEVERE, "Error listando usuarios.", ex);
            return new Respuesta(false, "Error listando usuarios.", "listarUsuarios " + ex.getMessage());
        }
    }
}
