package patmal.course.enigma.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import patmal.course.enigma.entity.MachineRotorEntity;
import patmal.course.enigma.server.dto.db.MachineRotorDTO;

@Mapper
public interface MachineRotorMapper {
    MachineRotorMapper INSTANCE = Mappers.getMapper(MachineRotorMapper.class);

    MachineRotorDTO entityToDto(MachineRotorEntity entity);
    MachineRotorEntity dtoToEntity(MachineRotorDTO dto);
}
