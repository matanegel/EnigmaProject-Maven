package patmal.course.enigma.server.dto.db;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@NoArgsConstructor
public class ProcessingDTO {
    private UUID machineId;

    private String sessionId;

    private String code;

    private String input;

    private String output;

    private long time;
}
