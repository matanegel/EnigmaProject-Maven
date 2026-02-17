package patmal.course.enigma.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import patmal.course.enigma.entity.MachineReflectorEntity;
import patmal.course.enigma.server.dto.db.MachineReflectorDTO;

@Mapper
public interface MachineReflectorMapper {
    MachineReflectorMapper INSTANCE = Mappers.getMapper(MachineReflectorMapper.class);

    MachineReflectorDTO entityToDto(MachineReflectorEntity entity);
    MachineReflectorEntity dtoToEntity(MachineReflectorDTO dto);
}