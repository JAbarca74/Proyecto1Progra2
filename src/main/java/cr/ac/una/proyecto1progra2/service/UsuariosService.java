// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
// 3) Servicio: UsuariosService.java
// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
package cr.ac.una.proyecto1progra2.service;

import cr.ac.una.proyecto1progra2.model.Usuarios;
import cr.ac.una.proyecto1progra2.DTO.UsuariosDto;
import cr.ac.una.proyecto1progra2.util.EntityManagerHelper;
import cr.ac.una.proyecto1progra2.util.Respuesta;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

public class UsuariosService {

    private final EntityManager em = EntityManagerHelper.getInstance().getManager();

    public Respuesta listarUsuarios() {
        try {
            TypedQuery<Usuarios> q =
                em.createNamedQuery("Usuarios.findAll", Usuarios.class);
            List<UsuariosDto> lista = q.getResultList()
                .stream()
                .map(UsuariosDto::new)
                .collect(Collectors.toList());
            return new Respuesta(true, "", "", "Usuarios", lista);
        } catch(Exception ex) {
            return new Respuesta(false, "Error listando usuarios.", ex.getMessage());
        }
    }

    public Respuesta getUsuario(String username, String password) {
        try {
            TypedQuery<Usuarios> q =
              em.createNamedQuery("Usuarios.findByCredentials", Usuarios.class);
            q.setParameter("username", username);
            q.setParameter("password", password);
            Usuarios u = q.getSingleResult();
            return new Respuesta(true, "", "", "Usuario", new UsuariosDto(u));
        } catch(NoResultException nre) {
            return new Respuesta(false, "Credenciales inválidas.", "getUsuario");
        } catch(Exception ex) {
            return new Respuesta(false, "Error obteniendo usuario.", ex.getMessage());
        }
    }

    public Respuesta guardarUsuario(UsuariosDto dto) {
        EntityTransaction tx = em.getTransaction();
        try {
<<<<<<< Updated upstream
            tx.begin();
            Usuarios u;
            if (dto.getId() != null) {
                u = em.find(Usuarios.class, dto.getId());
                if (u == null) {
                    tx.rollback();
                    return new Respuesta(false, "Usuario no encontrado.", "guardarUsuario");
=======
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
>>>>>>> Stashed changes
                }
            } else {
                u = new Usuarios();
            }
            u.actualizar(dto);
            if (dto.getId() == null) em.persist(u);
            else em.merge(u);
            tx.commit();
            return new Respuesta(true, "", "", "Usuario", new UsuariosDto(u));
        } catch(Exception ex) {
            if (tx.isActive()) tx.rollback();
            return new Respuesta(false, "Error guardando usuario.", ex.getMessage());
        }
    }
    

    public Respuesta eliminarUsuario(Long id) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Usuarios u = em.find(Usuarios.class, id);
            if (u != null) {
                em.remove(u);
                tx.commit();
                return new Respuesta(true, "", "", "Usuarios", null);
            } else {
                tx.rollback();
                return new Respuesta(false, "Usuario no encontrado.", "eliminarUsuario");
            }
        } catch(Exception ex) {
            if (tx.isActive()) tx.rollback();
            return new Respuesta(false, "Error eliminando usuario.", ex.getMessage());
        }
    }
}
