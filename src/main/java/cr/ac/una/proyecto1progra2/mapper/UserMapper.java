package cr.ac.una.proyecto1progra2.mapper;

import cr.ac.una.proyecto1progra2.dto.UserDTO;
import cr.ac.una.proyecto1progra2.model.Usuarios;
import java.math.BigDecimal;
import java.math.BigInteger;

public class UserMapper {
    public static UserDTO toDTO(Usuarios u) {
        if (u == null) return null;
        UserDTO dto = new UserDTO();
        dto.setId(u.getId().longValue());
        dto.setUsername(u.getUsername());
        dto.setRoleId(u.getRoleId().intValue());
        dto.setActive(u.getIsActive() != null && u.getIsActive() == 'Y');
        return dto;
    }
    public static Usuarios toEntity(UserDTO dto) {
        if (dto == null) return null;
        Usuarios u = new Usuarios();
        u.setId(BigDecimal.valueOf(dto.getId()));
        u.setUsername(dto.getUsername());
        // contraseña la manejas aparte
        u.setRoleId(BigInteger.valueOf(dto.getRoleId()));
        u.setIsActive(dto.isActive() ? 'Y' : 'N');
        return u;
    }
}
