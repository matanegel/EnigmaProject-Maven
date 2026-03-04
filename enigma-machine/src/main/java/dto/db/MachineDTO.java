package dto.db;

import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
public class MachineDTO {

    private String name;

    private int rotorsCount;

    private String abc;

    //private List<MachineRotorDTO> rotors;

    //private List<MachineReflectorDTO> reflectors;
}
