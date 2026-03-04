package patmal.course.enigma.server.dto.db;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
public class MachineRotorDTO {
    private UUID machineId;

    private Integer rotorId;

    private Integer notch;

    private String wiringRight;

    private String wiringLeft;
}
