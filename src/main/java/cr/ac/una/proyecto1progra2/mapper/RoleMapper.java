package cr.ac.una.proyecto1progra2.mapper;

import cr.ac.una.proyecto1progra2.dto.RoleDTO;
import cr.ac.una.proyecto1progra2.model.Roles;
import java.math.BigDecimal;

public class RoleMapper {
    public static RoleDTO toDTO(Roles r) {
        if (r == null) return null;
        RoleDTO dto = new RoleDTO();
        dto.setId(r.getId().longValue());
        dto.setName(r.getName());
        return dto;
    }
    public static Roles toEntity(RoleDTO dto) {
        if (dto == null) return null;
        Roles r = new Roles();
        r.setId(BigDecimal.valueOf(dto.getId()));
        r.setName(dto.getName());
        return r;
    }
}
