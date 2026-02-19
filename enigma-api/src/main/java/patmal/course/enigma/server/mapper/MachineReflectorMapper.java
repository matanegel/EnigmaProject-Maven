package patmal.course.enigma.server.mapper;
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
    @Mapping(target = "input", expression = "java(generateIndexSequence(reflector))")
    @Mapping(target = "output", expression = "java(generateValueSequence(reflector))")
    MachineReflectorEntity toEntity(Reflector reflector);

   //hepler methods to generate the input sequence
    default String generateIndexSequence(Reflector reflector) {
        int length = reflector.getWiringReflactor().getWiringRef().length;
        return java.util.stream.IntStream.range(0, length)
                .mapToObj(String::valueOf)
                .collect(java.util.stream.Collectors.joining(","));
    }

    // helper method to generate the output sequence
    default String generateValueSequence(Reflector reflector) {
        return java.util.Arrays.stream(reflector.getWiringReflactor().getWiringRef())
                .mapToObj(String::valueOf)
                .collect(java.util.stream.Collectors.joining(","));
    }

/*    default reflector_id_enum toRomeEnum(String id) {
        return switch (id) {
            case "I" -> reflector_id_enum.I;
            case "II" -> reflector_id_enum.II;
            case "III" -> reflector_id_enum.III;
            case "IV" -> reflector_id_enum.IV;
            case "V" -> reflector_id_enum.V;
            default -> throw new IllegalArgumentException("Invalid reflector ID: " + id);
        };
    }*/
}