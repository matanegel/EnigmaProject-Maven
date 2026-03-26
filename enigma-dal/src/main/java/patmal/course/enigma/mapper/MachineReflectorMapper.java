package patmal.course.enigma.mapper;
import hardware.WiringCables.WiringReflactor;
import hardware.parts.Reflector;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import patmal.course.enigma.entity.MachineReflectorEntity;
import storage.reflector.reflector_id_enum;

@Mapper(componentModel = "spring")
public interface MachineReflectorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "machineId", ignore = true)
    @Mapping(target = "reflectorId", source = "ID")
    @Mapping(target = "input", expression = "java(generateInputPairs(reflector))")
    @Mapping(target = "output", expression = "java(generateOutputPairs(reflector))")
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

    default String generateInputPairs(Reflector reflector) {
        int[] wiring = reflector.getWiringReflactor().getWiringRef();
        return java.util.stream.IntStream.range(0, wiring.length)
                // Filter: Only take the 'left' side of the pair where index < value
                // This ensures we get 1->25 but skip 25->1
                .filter(i -> i < wiring[i])
                .map(i -> i + 1) // 0-based to 1-based
                .mapToObj(val -> String.format("%02d", val)) // Pad for DB constraint
                .collect(java.util.stream.Collectors.joining(" "));
    }

    default String generateOutputPairs(Reflector reflector) {
        int[] wiring = reflector.getWiringReflactor().getWiringRef();
        return java.util.stream.IntStream.range(0, wiring.length)
                .filter(i -> i < wiring[i])
                .map(i -> wiring[i] + 1) // Get the value (output) + 1
                .mapToObj(val -> String.format("%02d", val)) // Pad for DB constraint
                .collect(java.util.stream.Collectors.joining(" "));
    }

    default WiringReflactor mapToWiringReflactor(MachineReflectorEntity entity) {
        if (entity == null || entity.getOutput() == null || entity.getInput() == null) {
            return null;
        }

        // 1. Clean the input/output strings ("01 02" -> "1 2")
        String cleanInput = normalizeSequence(entity.getInput());
        String cleanOutput = normalizeSequence(entity.getOutput());

        // 2. Calculate alphabet size (13 pairs * 2 = 26)
        // CRITICAL: .length was 13, you need 26 for the array!
        int alphabetSize = cleanInput.split("\\s+").length * 2;

        // 3. Pass to the constructor
        return new WiringReflactor(cleanInput, cleanOutput, alphabetSize);
    }

    // Ensure this is default so the Mapper implementation can use it
    @Named("doNormalize")
    default String normalizeSequence(String sequence) {
        if (sequence == null) return "";
        return java.util.Arrays.stream(sequence.trim().split("\\s+"))
                .map(s -> String.valueOf(Integer.parseInt(s)))
                .collect(java.util.stream.Collectors.joining(" "));
    }
}