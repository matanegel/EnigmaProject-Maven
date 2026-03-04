package patmal.course.enigma.server.dto;


import lombok.Data;

@Data
public class EncryptionOutputDTO {

    private String output;
    private String currentRotorsPositionCompact;
}
