package patmal.course.enigma.mapper;


import machine.Machine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import patmal.course.enigma.entity.MachineEntity;

@Mapper(componentModel = "spring")
public interface MachineMapperApi {

    @Mapping(source = "alphabet", target = "abc")
    MachineEntity machineToEntity(Machine machine);

    @Mapping(source = "abc", target = "alphabet")
    Machine entityToMachine(MachineEntity entity);
}
