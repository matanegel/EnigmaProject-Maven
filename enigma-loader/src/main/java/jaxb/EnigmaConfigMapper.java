package jaxb;


import hardware.WiringCables.WiringReflactor;
import hardware.WiringCables.WiringRotor;
import hardware.parts.Reflector;
import hardware.parts.Rotor;
import jaxb.config.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EnigmaConfigMapper {

    private final EnigmaConfig config;

    public EnigmaConfigMapper(EnigmaConfig config) {
        this.config = config;
    }

    /**
     * Builds a list of Router objects from the JAXB config.
     * Each RotorConfig from the XML is converted into a Router + Wiring.
     */
    public Map<Integer, Rotor> buildRotors() {

        String alphabet = config.getAlphabet();
        Map<Integer, Rotor> rotors = new HashMap<>();

        for (RotorConfig rotorCfg : config.getRotors()) {

            int id = rotorCfg.getId();
            int notch = rotorCfg.getNotch() - 1;

            // 1. Build right and left columns as strings from the positioning list
            StringBuilder rightCol = new StringBuilder();
            StringBuilder leftCol  = new StringBuilder();

            for (PositioningConfig pos : rotorCfg.getPositions()) {
                rightCol.append(pos.getRight());
                leftCol.append(pos.getLeft());
            }

            // 2. Build Wiring object according to the columns
            WiringRotor wiringRotor = new WiringRotor(rightCol.toString(), leftCol.toString(), alphabet);

            // 3. Build Router object, using your Router class
            Rotor rotor = new Rotor(id, notch, alphabet.length(), wiringRotor);
            // router.setRingSetting(0);  // if you have ring setting

            // 4. Add to the result list
            rotors.put(id, rotor);
        }

        return rotors;
    }

    public Map<String, Reflector> buildReflectors() {
        String alphabet = config.getAlphabet();


        List<ReflectorConfig> reflectorsList = config.getReflectors();
        if (reflectorsList == null || reflectorsList.isEmpty()) {
            return Collections.emptyMap();
        }

        Map<String, Reflector> reflectors = new HashMap<>();
        for (ReflectorConfig reflectorConfig : reflectorsList) {
            String id = reflectorConfig.getId();
            StringBuilder leftCol = new StringBuilder();
            StringBuilder rightCol = new StringBuilder();

            List<ReflectMappingConfig> mappings = reflectorConfig.getMappings();
            if (mappings != null) {
                for (ReflectMappingConfig m : mappings) {
                    rightCol.append(m.getInput());
                    rightCol.append(" ");
                    leftCol.append(m.getOutput());
                    leftCol.append(" ");
                }
            }

            WiringReflactor wiring = new WiringReflactor(rightCol.toString(),leftCol.toString(), alphabet.length());
            Reflector reflector = new Reflector(id, wiring);
            reflectors.put(id, reflector);
        }

        return reflectors;
    }

    public EnigmaConfig getEnigmaConfig() {
        return this.config;
    }
}
