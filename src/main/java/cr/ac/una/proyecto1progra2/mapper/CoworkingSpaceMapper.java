package cr.ac.una.proyecto1progra2.mapper;

import cr.ac.una.proyecto1progra2.dto.CoworkingSpaceDTO;
import cr.ac.una.proyecto1progra2.model.CoworkingSpaces;
import java.math.BigDecimal;
import java.math.BigInteger;

public class CoworkingSpaceMapper {
    public static CoworkingSpaceDTO toDTO(CoworkingSpaces c) {
        if (c == null) return null;
        CoworkingSpaceDTO dto = new CoworkingSpaceDTO();
        dto.setId(c.getId().longValue());
        dto.setName(c.getName());
        dto.setCapacity(c.getCapacity() != null 
            ? c.getCapacity().intValue() : 0);
        dto.setSpaceId(c.getSpaceId().getId().longValue());
        dto.setTypeId(c.getTypeId().getId().longValue());
        return dto;
    }
}
