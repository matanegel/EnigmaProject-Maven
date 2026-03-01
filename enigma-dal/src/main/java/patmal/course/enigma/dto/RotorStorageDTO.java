package patmal.course.enigma.dto;

import hardware.parts.Rotor;

import java.util.Map;

public class RotorStorageDTO {
    private final Map<Integer, Rotor> rotorMap;

    public RotorStorageDTO(Map<Integer, Rotor> rotorMap) {
        this.rotorMap = rotorMap;
    }
}
