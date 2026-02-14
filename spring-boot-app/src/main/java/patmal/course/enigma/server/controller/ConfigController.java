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
        return enigmaRunTime.order2CreateStatusDTO(verbose);
    }

    @PutMapping("/manual")
    public ResponseEntity<String> getManualConfig(@RequestBody EnigmaManualConfigDTO manualConfig) {
        String initCode = enigmaRunTime.order3GetManualConfig(manualConfig);
        return ResponseEntity.ok(initCode);
    }

    @PutMapping("/automatic")
    public ResponseEntity<String> getAutomaticConfig() {

        String initCode = enigmaRunTime.order4GetAutomaticConfig();
        return ResponseEntity.ok(initCode);
    }

    @PutMapping("/reset")
    public ResponseEntity<String> resetConfigurationToOriginal(){
        if (enigmaRunTime.getMachine().getEngine() == null) {
            throw new UnsupportedOperationException("Engine Not Configured Yet - Make Order 3/4 First");
        }
        Rotor[] rotors = enigmaRunTime.getMachine().getEngine().getRotorsManagers().getRotors();
        List<Character> originalPosition = enigmaRunTime.getStorageManager().getOriginalPosition();
        for (int i = 0; i < rotors.length; i++) {
            rotors[i].setPosition(rotors[i].getWiringRotor().getIndexOfChInRightColumn(originalPosition.get(i)));
        }
        String initCode = enigmaRunTime.getCodeBuilder().getCode(true);
        return ResponseEntity.ok(initCode);
    }

    public void printEngine(){
        Engine engine = enigmaRunTime.getMachine().getEngine();

        for (Rotor rotor : engine.getRotorsManagers().getRotors()) {
            System.out.println("Rotor " + rotor.getID() + " position: " + rotor.getPosition());
            System.out.println("wiring: " + rotor.getWiringRotor().toString());

        }
    }
}

