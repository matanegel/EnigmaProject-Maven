package patmal.course.enigma.server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnigmaStatusDTO {
    private int totalRotors;
    private int totalReflectors;
    private int totalProcessedMessages;
    // Detailed objects for verbose = true
    private DetailedConfig originalCode;
    private DetailedConfig currentRotorsPosition;
    // end of verbose objects

    private String originalCodeCompact;
    private String currentRotorsPositionCompact;

    @Data @Builder
    public static class DetailedConfig {
        private List<RotorDetail> rotors;
        private String reflector;
        private List<PlugPair> plugs;
    }

    @Data @AllArgsConstructor
    public static class RotorDetail {
        private int rotorNumber;
        private String rotorPosition;
        private int notchDistance;
    }

    @Data @AllArgsConstructor
    public static class PlugPair {
        private String plug1;
        private String plug2;
    }
}
