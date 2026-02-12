package patmal.course.enigma.console;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EnigmaStatusDTO {
    public int totalRotors;
    public int totalReflectors;
    public int totalProcessedMessages;
    public String originalCodeCompact;
    public String currentRotorsPositionCompact;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Object originalCode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public Object currentRotorsPosition;
}
