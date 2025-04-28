package cr.ac.una.proyecto1progra2.service;

import cr.ac.una.proyecto1progra2.model.Roles;
import cr.ac.una.proyecto1progra2.model.RolesDto;
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

public class RolesService {

    EntityManager em = EntityManagerHelper.getInstance().getManager();
    private EntityTransaction et;

    public Respuesta getRol(Long id) {
        try {
            Query qryRol = em.createNamedQuery("Roles.findById", Roles.class);
            qryRol.setParameter("id", id);
            RolesDto rolDto = new RolesDto((Roles) qryRol.getSingleResult());
            return new Respuesta(true, "", "", "Rol", rolDto);
        } catch (NoResultException ex) {
            return new Respuesta(false, "No existe el rol con el id ingresado.", "getRol NoResultException");
        } catch (Exception ex) {
            Logger.getLogger(RolesService.class.getName()).log(Level.SEVERE, "Error obteniendo el rol [" + id + "]", ex);
            return new Respuesta(false, "Error obteniendo el rol.", "getRol " + ex.getMessage());
        }
    }

    public Respuesta guardarRol(RolesDto rolDto) {
        try {
            et = em.getTransaction();
            et.begin();
            Roles rol;
            if (rolDto.getId() != null && rolDto.getId() > 0) {
                rol = em.find(Roles.class, rolDto.getId());
                if (rol == null) {
                    et.rollback();
                    return new Respuesta(false, "No se encontró el rol a modificar.", "guardarRol NoResultException");
                }
                rol.actualizar(rolDto);
                rol = em.merge(rol);
            } else {
                rol = new Roles(rolDto);
                em.persist(rol);
            }
            et.commit();
            return new Respuesta(true, "", "", "Rol", new RolesDto(rol));
        } catch (Exception ex) {
            et.rollback();
            Logger.getLogger(RolesService.class.getName()).log(Level.SEVERE, "Ocurrió un error al guardar el rol.", ex);
            return new Respuesta(false, "Ocurrió un error al guardar el rol.", "guardarRol " + ex.getMessage());
        }
    }

    public Respuesta eliminarRol(Long id) {
        try {
            et = em.getTransaction();
            et.begin();
            Roles rol;
            if (id != null && id > 0) {
                rol = em.find(Roles.class, id);
                if (rol == null) {
                    et.rollback();
                    return new Respuesta(false, "No se encontró el rol a eliminar.", "eliminarRol NoResultException");
                }
                em.remove(rol);
            } else {
                et.rollback();
                return new Respuesta(false, "Debe cargar el rol a eliminar.", "eliminarRol NoResultException");
            }
            et.commit();
            return new Respuesta(true, "", "");
        } catch (Exception ex) {
            et.rollback();
            if (ex.getCause() != null && ex.getCause().getCause() instanceof SQLIntegrityConstraintViolationException) {
                return new Respuesta(false, "No se puede eliminar el rol porque tiene relaciones con otros registros.", "eliminarRol " + ex.getMessage());
            }
            Logger.getLogger(RolesService.class.getName()).log(Level.SEVERE, "Error eliminando el rol.", ex);
            return new Respuesta(false, "Error eliminando el rol.", "eliminarRol " + ex.getMessage());
        }
    }

    public Respuesta listarRoles() {
        try {
            Query qry = em.createNamedQuery("Roles.findAll", Roles.class);
            List<Roles> roles = qry.getResultList();
            List<RolesDto> rolesDto = new ArrayList<>();
            for (Roles rol : roles) {
                rolesDto.add(new RolesDto(rol));
            }
            return new Respuesta(true, "", "", "Roles", rolesDto);
        } catch (Exception ex) {
            Logger.getLogger(RolesService.class.getName()).log(Level.SEVERE, "Error listando roles.", ex);
            return new Respuesta(false, "Error listando roles.", "listarRoles " + ex.getMessage());
        }
    }
}
