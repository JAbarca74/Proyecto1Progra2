package cr.ac.una.proyecto1progra2.service;

import cr.ac.una.proyecto1progra2.dto.SpaceDTO;
import cr.ac.una.proyecto1progra2.dto.SpaceTypeDTO;
import cr.ac.una.proyecto1progra2.mapper.SpaceMapper;
import cr.ac.una.proyecto1progra2.mapper.SpaceTypeMapper;
import cr.ac.una.proyecto1progra2.model.Spaces;
import cr.ac.una.proyecto1progra2.model.SpaceTypes;
import cr.ac.una.proyecto1progra2.utils.JPAUtil;
import cr.ac.una.proyecto1progra2.utils.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar datos y operaciones relacionadas con espacios y tipos de espacios.
 */
public class SpaceService {

    /**
     * Obtiene todos los tipos de espacio (entidades JPA).
     */
    public List<SpaceTypes> findAllSpaceTypes() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<SpaceTypes> query =
                em.createNamedQuery("SpaceTypes.findAll", SpaceTypes.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Busca un tipo de espacio por su ID.
     */
    public SpaceTypes findSpaceTypeById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(SpaceTypes.class, BigDecimal.valueOf(id));
        } finally {
            em.close();
        }
    }

    /**
     * Obtiene todos los tipos de espacio y los convierte a DTO.
     */
    public List<SpaceTypeDTO> findAllSpaceTypesDTO() {
        return findAllSpaceTypes().stream()
            .map(SpaceTypeMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Obtiene todas las entidades Spaces (JPA).
     */
    public List<Spaces> findAllSpaces() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Spaces> query =
                em.createNamedQuery("Spaces.findAll", Spaces.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    /**
     * Busca un espacio por su ID.
     */
    public Spaces findSpaceById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Spaces.class, BigDecimal.valueOf(id));
        } finally {
            em.close();
        }
    }

    /**
     * Obtiene todos los espacios y los convierte a DTO.
     */
    public List<SpaceDTO> findAllSpacesDTO() {
        return findAllSpaces().stream()
            .map(SpaceMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Busca un espacio por su ID y lo convierte a DTO.
     */
    public SpaceDTO findSpaceByIdDTO(Long id) {
        Spaces entity = findSpaceById(id);
        return SpaceMapper.toDTO(entity);
    }
}
