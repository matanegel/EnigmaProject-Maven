package patmal.course.enigma.server.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import patmal.course.enigma.entity.ProcessingEntity;
import patmal.course.enigma.server.dto.db.ProcessingDTO;

@Mapper
public interface ProcessingMapper {
    MachineRotorMapper INSTANCE = Mappers.getMapper(MachineRotorMapper.class);

    ProcessingDTO entityToDto(ProcessingEntity entity);
    ProcessingEntity dtoToEntity(ProcessingDTO dto);
}
