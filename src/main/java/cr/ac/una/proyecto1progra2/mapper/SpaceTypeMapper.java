package cr.ac.una.proyecto1progra2.mapper;

import cr.ac.una.proyecto1progra2.dto.SpaceTypeDTO;
import cr.ac.una.proyecto1progra2.model.SpaceTypes;
import java.math.BigDecimal;

public class SpaceTypeMapper {
    public static SpaceTypeDTO toDTO(SpaceTypes s) {
        if (s == null) return null;
        SpaceTypeDTO dto = new SpaceTypeDTO();
        dto.setId(s.getId().longValue());
        dto.setTypeName(s.getTypeName());
        return dto;
    }
    public static SpaceTypes toEntity(SpaceTypeDTO dto) {
        if (dto == null) return null;
        SpaceTypes s = new SpaceTypes();
        s.setId(BigDecimal.valueOf(dto.getId()));
        s.setTypeName(dto.getTypeName());
        return s;
    }
}
