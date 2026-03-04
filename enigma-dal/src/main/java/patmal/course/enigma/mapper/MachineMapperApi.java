package patmal.course.enigma.mapper;


import hardware.parts.Reflector;
import hardware.parts.Rotor;
import machine.Machine;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import patmal.course.enigma.entity.MachineEntity;
import storage.StorageManager;
import storage.reflector.ReflectorStorage;
import storage.rotor.RotorStorage;

import java.util.*;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {MachineReflectorMapper.class})
public interface MachineMapperApi {

    @Mapping(source = "alphabet", target = "abc")
    MachineEntity machineToEntity(Machine machine);

    @Mapping(source = "abc", target = "alphabet")
    Machine entityToMachine(MachineEntity entity);

    @Mapping(target = "machineName", source = "name")
    @Mapping(target = "ABC", source = "abc")
    @Mapping(target = "rotorsCount", source = "rotorsCount")
    @Mapping(target = "RS", expression = "java(mapToRotorStorage(entity, rotorMapper))")
    @Mapping(target = "RFS", expression = "java(mapToReflectorStorage(entity, reflectorMapper))")
    StorageManager toStorageManager(MachineEntity entity,
                                    @Context MachineRotorMapper rotorMapper,
                                    @Context MachineReflectorMapper reflectorMapper);

    // --- Internal Logic for RotorStorage ---
    default RotorStorage mapToRotorStorage(MachineEntity entity, MachineRotorMapper rotorMapper) {
        if (entity.getRotors() == null) return null;

        Map<Integer, Rotor> map = entity.getRotors().stream()
                .map(rotorEntity -> rotorMapper.toRotor(rotorEntity, entity.getAbc()))
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Rotor::getID, r -> r, (e, r) -> e));

        return new RotorStorage(map);
    }

    // --- Internal Logic for ReflectorStorage ---
    default ReflectorStorage mapToReflectorStorage(MachineEntity entity, MachineReflectorMapper reflectorMapper) {

        if (entity.getReflectors() == null) return null;

        Map<String, Reflector> map = entity.getReflectors().stream()
                .map(reflectorMapper::toReflector)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(Reflector::getID, r -> r, (e, r) -> e));

        return new ReflectorStorage(map);
    }
}
