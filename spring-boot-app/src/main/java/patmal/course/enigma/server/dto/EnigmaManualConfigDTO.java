package patmal.course.enigma.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class EnigmaManualConfigDTO {
    private List<RotorSelectionDTO> rotors;
    private String reflector;
    private List<PlugPairDTO> plugs;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RotorSelectionDTO {
        private int rotorNumber;
        private String rotorPosition;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlugPairDTO {
        private String plug1;
        private String plug2;
    }
}
