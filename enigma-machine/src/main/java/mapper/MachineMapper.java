package mapper;

import dto.db.MachineDTO;
import machine.Machine;

public class MachineMapper {

    public static MachineDTO toDTO(Machine machine) {
        if (machine == null) return null;
        MachineDTO dto = new MachineDTO();
        dto.setName(machine.getName());
        dto.setRotorsCount(machine.getRotorsCount());
        dto.setAbc(machine.getAlphabet());
        return dto;
    }
}
