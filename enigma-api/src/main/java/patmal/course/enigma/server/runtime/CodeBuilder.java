package patmal.course.enigma.server.runtime;

import hardware.Utils;
import hardware.parts.Rotor;
import lombok.Setter;
import machine.Machine;
import patmal.course.enigma.server.dto.EnigmaStatusDTO;
import storage.StorageManager;

import java.util.ArrayList;
import java.util.List;

@Setter
public class CodeBuilder {
    private final Machine enigmaMachine;
    private StorageManager SM;

    public CodeBuilder(Machine enigmaMachine, StorageManager SM) {
        this.enigmaMachine = enigmaMachine;
        this.SM = SM;
    }
    public String getCode(boolean original) {
        if (enigmaMachine.getEngine() == null) {
            return "Machine Not Configured Yet - No Code Available";
        }
        return '<' +
                buildRotorString() +
                '>' +
                '<' +
                buildPositionString(original) +
                '>' +
                '<' +
                buildReflectorString() +
                '>' +
                '<' +
                buildPlugboardString() +
                '>';

    }

    public String buildRotorString() {
        StringBuilder sb = new StringBuilder();
        Rotor[] rotors = enigmaMachine.getEngine().getRotorsManagers().getRotors();
        int index = rotors.length - 1;
        for (int i = index; i >= 0; i--) {
            sb.append(rotors[i].getID());
            sb.append(',');
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public String buildReflectorString() {
        return enigmaMachine.getEngine().getReflectorId();
    }

    public String buildPositionString(boolean original) {
        StringBuilder sb = new StringBuilder();
        Rotor[] rotors = enigmaMachine.getEngine().getRotorsManagers().getRotors();
        List<Character> originalPosition = SM.getOriginalPosition();
        int index = rotors.length - 1;
        for (int i = index; i >= 0; i--) {
            sb.append(original ? originalPosition.get(i) :
                    rotors[i].getWiringRotor().getRightColumn()
                            .charAt(rotors[i].getPosition()));
            sb.append('(');
            int res = rotors[i].getNoche()
                    - (original ? Utils.charToIndex(originalPosition.get(i), rotors[i]
                    .getWiringRotor().getRightColumn())
                    : rotors[i].getPosition());
            sb.append(Utils.normalize(res, rotors[i].sizeABC));
            sb.append(')');
            sb.append(',');
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public String buildPlugboardString() {
        String config = enigmaMachine.getEngine().getPlugboard().getConfigString();
        return config.isEmpty() ? "" : config;
    }

    public EnigmaStatusDTO.DetailedConfig buildDetailedConfig(boolean original) {
        if (enigmaMachine.getEngine() == null) {
            return null;
        }

        Rotor[] rotors = enigmaMachine.getEngine().getRotorsManagers().getRotors();
        List<Character> originalPosition = SM.getOriginalPosition();
        List<EnigmaStatusDTO.RotorDetail> rotorList = new ArrayList<>();

        // Logic mirrored from your buildPositionString
        int index = rotors.length - 1;
        for (int i = index; i >= 0; i--) {
            // 1. Determine the character and its index
            char posChar = original ? originalPosition.get(i) : SM.getABC().charAt(rotors[i].getPosition());
            int posIdx = original ? Utils.charToIndex(posChar, SM.getABC()) : rotors[i].getPosition();

            // 2. Your specific Notch Distance algorithm
            int res = rotors[i].getNoche() - posIdx;
            int distance = Utils.normalize(res, rotors[i].sizeABC);

            // 3. Add to the structured list
            rotorList.add(new EnigmaStatusDTO.RotorDetail(
                    rotors[i].getID(),
                    String.valueOf(posChar),
                    distance
            ));
        }

        return EnigmaStatusDTO.DetailedConfig.builder()
                .rotors(rotorList)
                .reflector(enigmaMachine.getEngine().getReflectorId())
                .plugs(parsePlugs())
                .build();
    }

    private List<EnigmaStatusDTO.PlugPair> parsePlugs() {
        List<EnigmaStatusDTO.PlugPair> pairs = new ArrayList<>();
        // Reusing your logic from buildPlugboardString
        String config = buildPlugboardString();

        if (config != null && !config.isEmpty()) {
            // Assumes your config string is pairs separated by comma like "AB,CD"
            String[] split = config.split(",");
            for (String pair : split) {
                if (pair.length() >= 2) {
                    pairs.add(new EnigmaStatusDTO.PlugPair(
                            String.valueOf(pair.charAt(0)),
                            String.valueOf(pair.charAt(1))
                    ));
                }
            }
        }
        return pairs;
    }
}
