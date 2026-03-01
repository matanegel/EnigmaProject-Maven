package patmal.course.enigma.mapper;
import hardware.WiringCables.WiringReflactor;
import hardware.parts.Reflector;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import patmal.course.enigma.entity.MachineReflectorEntity;
import storage.reflector.reflector_id_enum;

@Mapper(componentModel = "spring")
public interface MachineReflectorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "machineId", ignore = true)
    @Mapping(target = "reflectorId", source = "ID")
    @Mapping(target = "input", source = "wiringReflactor.input")
    @Mapping(target = "output", source = "wiringReflactor.output")
    MachineReflectorEntity toEntity(Reflector reflector);

    default Reflector toReflector(MachineReflectorEntity entity) {
        if (entity == null) {
            return null;
        }

        String id = entity.getReflectorId().name();
        WiringReflactor wiring = mapToWiringReflactor(entity);

        // Explicitly calling your constructor solves the "No write accessor" error
        return new Reflector(id, wiring);
    }

    // Helper methods to generate the 1-based sequence for the DB
    default String generateIndexSequence(Reflector reflector) {
        int length = reflector.getWiringReflactor().getWiringRef().length;
        return java.util.stream.IntStream.range(0, length)
                .map(i -> i + 1) // Shift 0-index to 1-based (e.g., 0 becomes "1")
                .mapToObj(String::valueOf)
                .collect(java.util.stream.Collectors.joining(" "));
    }

    default String generateValueSequence(Reflector reflector) {
        return java.util.Arrays.stream(reflector.getWiringReflactor().getWiringRef())
                .map(val -> val + 1) // Shift internal 0-value to 1-based (e.g., 0 becomes "1")
                .mapToObj(String::valueOf)
                .collect(java.util.stream.Collectors.joining(" "));
    }

    // Helper to build the domain object from the Entity strings
    default WiringReflactor mapToWiringReflactor(MachineReflectorEntity entity) {
        if (entity.getOutput() == null || entity.getInput() == null) return null;

        int alphabetSize = entity.getInput().trim().split("[,\\s]+").length * 2;

        // Passing the ABCD strings to your constructor
        return new WiringReflactor(entity.getInput(), entity.getOutput(), alphabetSize);
    }
}