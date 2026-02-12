package patmal.course.enigma.server.controller;

import hardware.engine.Engine;
import hardware.engine.rotorsManagers;
import hardware.parts.Plugboard;
import hardware.parts.Reflector;
import hardware.parts.Rotor;
import history.ConfigurationStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patmal.course.enigma.server.dto.EnigmaManualConfigDTO;
import patmal.course.enigma.server.dto.EnigmaStatusDTO;
import patmal.course.enigma.server.runtime.EnigmaRunTime;
import software.AutoConfig;
import software.MachineConfig;
import software.ManualConfig;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/config")
public class ConfigController {

    private EnigmaRunTime enigmaRunTime;

    @Autowired
    public ConfigController(EnigmaRunTime enigmaRunTime) {
        this.enigmaRunTime = enigmaRunTime;
    }

    @GetMapping
    public EnigmaStatusDTO getMachineConfig(
            @RequestParam(name = "verbose", defaultValue = "false") boolean verbose
    ) {
        return enigmaRunTime.createStatusDTO(verbose);
    }

    @PutMapping("/manual")
    public ResponseEntity<String> getManualConfig(@RequestBody EnigmaManualConfigDTO manualConfig) {
        List<Character> positions = new ArrayList<>();
        Set<Integer> usedRotors = new HashSet<>();
        List<Rotor> rotors = new ArrayList<>();
        Plugboard plugBoard = new Plugboard(enigmaRunTime.getStorageManager().getABC());
        if (!enigmaRunTime.getSupplyLoaded()) {
            throw new UnsupportedOperationException("XML File Not Loaded Yet - Make Order 1 First");
        }


        for (EnigmaManualConfigDTO.RotorSelectionDTO rotorSelection : manualConfig.getRotors()) {
            int rotorId = rotorSelection.getRotorNumber();
            if (usedRotors.contains(rotorId)) {
                throw new IllegalStateException("Cannot build enigma machine with duplicate rotors");
            }
            usedRotors.add(rotorId);
            rotors.add(enigmaRunTime.getStorageManager().optionalGetRotorByID(rotorId));
            if (rotorSelection.getRotorPosition() != null && !rotorSelection.getRotorPosition().isEmpty()) {
                positions.add(rotorSelection.getRotorPosition().charAt(0));
            }
        }
        rotors = new ArrayList<>(rotors.reversed());

        if (rotors.size() != positions.size()) {
            throw new IllegalArgumentException("Number of positions must match rotors amount");
        }


        enigmaRunTime.getStorageManager().setOriginalPosition(positions);
        Reflector reflector = enigmaRunTime.getStorageManager().optionalGetReflectorByID(manualConfig.getReflector());


        try {
            if (!manualConfig.getPlugs().isEmpty()) {
                StringBuilder plugStringBuilder = new StringBuilder();
                for (EnigmaManualConfigDTO.PlugPairDTO plugPair : manualConfig.getPlugs()) {
                    plugStringBuilder.append(plugPair.getPlug1());
                    plugStringBuilder.append(plugPair.getPlug2());
                }

                String pipeFormat = Plugboard.parsePairFormat(plugStringBuilder.toString());
                plugBoard = new Plugboard(enigmaRunTime.getStorageManager().getABC(), pipeFormat);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid plugboard configuration: " + e.getMessage());
        }


        int rotorsCount = enigmaRunTime.getStorageManager().getRotorsCount();
        rotorsManagers manager = new rotorsManagers(rotors.toArray(new Rotor[0]));
        List<Integer> indexOfPositions = manager.MappingInputCharPositionByRightColumnToIndex(positions);
        manager.setRotorsPosition(indexOfPositions);

        enigmaRunTime.getMachine().setEngine(new Engine(rotorsCount, reflector, manager, plugBoard, enigmaRunTime.getStorageManager().getABC()));


        String initCode = enigmaRunTime.getCodeBuilder().getCode(true);
        ConfigurationStats state = new ConfigurationStats(initCode);
        enigmaRunTime.getMachine().getFullHistory().add(state);
        return ResponseEntity.ok(initCode);
    }

    @PutMapping("/automatic")
    public ResponseEntity<String> getAutomaticConfig() {

        if (!enigmaRunTime.getSupplyLoaded()) {
            throw new UnsupportedOperationException("XML File Not Loaded Yet - Make Order 1 First");
        }
        MachineConfig machineConfiguration = new AutoConfig(enigmaRunTime.getStorageManager());
        enigmaRunTime.getMachine().setEngine(machineConfiguration.configureAndGetEngine());
        String initCode = enigmaRunTime.getCodeBuilder().getCode(true);
        ConfigurationStats state = new ConfigurationStats(initCode);
        enigmaRunTime.getMachine().getFullHistory().add(state);
        return ResponseEntity.ok(initCode);
    }



}
