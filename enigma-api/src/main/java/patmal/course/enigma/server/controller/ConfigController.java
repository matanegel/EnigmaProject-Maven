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
public class ConfigController extends EnigmaController {

    public ConfigController(EnigmaRunTime enigmaRunTime) {
        super(enigmaRunTime);
    }

    @GetMapping("/config")
    public EnigmaStatusDTO getMachineConfig(
            @RequestParam(name = "verbose", defaultValue = "false") boolean verbose
    ) {
        return this.getEnigmaRunTime().order2CreateStatusDTO(verbose);
    }

    @PutMapping("/config/manual")
    public ResponseEntity<String> putManualConfig(@RequestBody EnigmaManualConfigDTO manualConfig) {
        String responseStr = this.getEnigmaRunTime().order3GetManualConfig(manualConfig);
        return ResponseEntity.ok(responseStr);
    }

    @PutMapping("/config/automatic")
    public ResponseEntity<String> putAutomaticConfig() {

        String responseStr = this.getEnigmaRunTime().order4GetAutomaticConfig();
        return ResponseEntity.ok(responseStr);
    }

    @PutMapping("/config/reset")
    public ResponseEntity<String> resetConfigurationToOriginal(){
       String resposeStr = this.getEnigmaRunTime().order6RestartMachineConfig();
        return ResponseEntity.ok(resposeStr);
    }

    public void printEngine(){
        Engine engine = this.getEnigmaRunTime().getMachine().getEngine();

        for (Rotor rotor : engine.getRotorsManagers().getRotors()) {
            System.out.println("Rotor " + rotor.getID() + " position: " + rotor.getPosition());
            System.out.println("wiring: " + rotor.getWiringRotor().toString());

        }
    }
}

