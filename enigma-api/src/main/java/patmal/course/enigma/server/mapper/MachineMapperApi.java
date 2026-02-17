package patmal.course.enigma.server.mapper;

import dto.db.MachineDTO;
import machine.Machine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import patmal.course.enigma.entity.MachineEntity;

@Mapper(componentModel = "spring")
public interface MachineMapperApi {
    MachineDTO entityToDto(MachineEntity entity);
    MachineEntity dtoToEntity(MachineDTO dto);

    @Mapping(source = "alphabet", target = "abc")
    MachineEntity machineToEntity(Machine machine);
}
