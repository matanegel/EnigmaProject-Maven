package patmal.course.enigma.server.controller;

import hardware.engine.Engine;
import hardware.parts.Rotor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import patmal.course.enigma.server.dto.EnigmaManualConfigDTO;
import patmal.course.enigma.server.dto.EnigmaStatusDTO;
import patmal.course.enigma.server.runtime.EnigmaRunTime;


import java.util.List;

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
    public ResponseEntity<String> putManualConfig(@RequestBody EnigmaManualConfigDTO manualConfig) {
        String responseStr = enigmaRunTime.order3GetManualConfig(manualConfig);
        return ResponseEntity.ok(responseStr);
    }

    @PutMapping("/automatic")
    public ResponseEntity<String> putAutomaticConfig() {

        String responseStr = enigmaRunTime.order4GetAutomaticConfig();
        return ResponseEntity.ok(responseStr);
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

