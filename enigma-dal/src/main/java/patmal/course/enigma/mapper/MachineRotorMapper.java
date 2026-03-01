package patmal.course.enigma.mapper;

import hardware.WiringCables.WiringRotor;
import hardware.parts.Rotor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import patmal.course.enigma.entity.MachineRotorEntity;


@Mapper(componentModel = "spring")
public interface MachineRotorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "machineId", ignore = true)
    @Mapping(target = "rotorId", source = "ID")
    @Mapping(target = "notch", source = "noche")
    @Mapping(target = "wiringRight", source = "wiringRotor.rightColumn")
    @Mapping(target = "wiringLeft", source = "wiringRotor.leftColumn")
    MachineRotorEntity toEntity(Rotor rotor);



    default Rotor toRotor(MachineRotorEntity entity, String alphabet) {
        if (entity == null) return null;

        WiringRotor wiring = new WiringRotor(
                entity.getWiringRight(),
                entity.getWiringLeft(),
                alphabet
        );

        return new Rotor(
                entity.getRotorId(),
                entity.getNotch(),
                alphabet.length(),
                wiring
        );
    }
}
