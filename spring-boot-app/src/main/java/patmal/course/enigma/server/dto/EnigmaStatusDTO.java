package patmal.course.enigma.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnigmaStatusDTO {
    private int rotorsAmount;
    private int reflectorsAmount;
    private int encryptionCount;
    private String originalPositions;
    private String currentCode;

    // Detailed objects for verbose = true
    private DetailedConfig originalCodeVerbose;
    private DetailedConfig currentCodeVerbose;

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
