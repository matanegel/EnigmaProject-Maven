package patmal.course.enigma.server.dto.db;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
public class MachineReflectorDTO {
    private UUID machineId;

    private String reflectorId; // Maps to your reflector_id_enum

    private String input;

    private String output;
}
