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
            @RequestParam(name =  "sessionID") String sessionID,
            @RequestParam(name = "verbose", defaultValue = "false") boolean verbose
    ) {
        return this.getEnigmaRunTime().order2CreateStatusDTO(sessionID, verbose);
    }

    @PutMapping("/config/manual")
    public ResponseEntity<String> putManualConfig(@RequestBody EnigmaManualConfigDTO manualConfig) {
        String responseStr = this.getEnigmaRunTime().order3GetManualConfig(manualConfig);
        return ResponseEntity.ok(responseStr);
    }

    @PutMapping("/config/automatic")
    public ResponseEntity<String> putAutomaticConfig(@RequestParam(name = "sessionID") String sessionID) {

        String responseStr = this.getEnigmaRunTime().order4GetAutomaticConfig(sessionID);
        return ResponseEntity.ok(responseStr);
    }

    @PutMapping("/config/reset")
    public ResponseEntity<String> resetConfigurationToOriginal(@RequestParam(name = "sessionID") String sessionID){
       String resposeStr = this.getEnigmaRunTime().order6RestartMachineConfig(sessionID);
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

