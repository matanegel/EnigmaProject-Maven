package storage;

import jaxb.config.PositioningConfig;
import jaxb.config.ReflectorConfig;
import jaxb.config.RotorConfig;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PartsConfigValidator {
    public static Set<Integer> usedRoutorIds = new HashSet<>();
    public static Set<Integer> usedReflectorIds = new HashSet<>();



    public PartsConfigValidator() {
    }

    public boolean runRotorValidation(RotorConfig rotor) {
        int id = rotor.getID();
        List<PositioningConfig> wires = rotor.getWires();



        boolean pass;
        pass = idInRange(1,Integer.MAX_VALUE, id) && rotorIdNotUsed(rotor);
        return pass;
    }

    public boolean runReflectorValidation(ReflectorConfig reflector) {
        int id = reflector.getID();
        if (id == -1) {
            throw new IllegalArgumentException("Reflector ID: " + reflector.getId() + " is invalid.");
        }
        return reflectorIdNotUsed(reflector);
    }

    private boolean rotorIdNotUsed(RotorConfig rotor) {
        if (!usedRoutorIds.contains(rotor.getID())) {
            usedRoutorIds.add(rotor.getID());
            return true;
        }
        throw new IllegalArgumentException("Rotor ID: " + rotor.getID() + " is used more than once.");
    }
    private boolean reflectorIdNotUsed(ReflectorConfig reflector) {
        if (!usedReflectorIds.contains(reflector.getID())) {
            usedReflectorIds.add(reflector.getID());
            return true;
        }
        throw new IllegalArgumentException("Reflector ID: " + reflector.getID() + " is used more than once.");
    }

    private boolean idInRange(int from, int to, int id) {
        return id >= from && id <= to;
    }

    public boolean rotorIdsHasNoHoles() {
        for (int i = 1; i <= usedRoutorIds.size(); i++) {
            if (!usedRoutorIds.contains(i)) {
                throw new IllegalArgumentException("Rotor IDs have holes. Missing ID: " + i);
            }
        }
        return true;
    }

    public boolean reflectorIdsHasNoHoles() {
        for (int i = 1; i <= usedReflectorIds.size(); i++) {
            if (!usedReflectorIds.contains(i)) {
                throw new IllegalArgumentException("Reflector IDs have holes. Missing ID: " + i);
            }
        }
        return true;
    }

    public static void reset() {
        if (usedRoutorIds != null) {
            usedRoutorIds.clear();
        }
        if (usedReflectorIds != null) {
            usedReflectorIds.clear();
        }
    }
}
