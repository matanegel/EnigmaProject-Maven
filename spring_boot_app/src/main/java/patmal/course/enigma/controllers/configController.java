package patmal.course.enigma.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import patmal.course.enigma.console.EnigmaStatusDTO;
import patmal.course.enigma.console.MachineManager;

@RestController
@RequestMapping("/enigma/config")
public class ConfigController {

    private final MachineManager machineManager;

    @Autowired
    public ConfigController(MachineManager machineManager) {
        this.machineManager = machineManager;
    }

    @GetMapping()
    public ResponseEntity<?> getMachineStatus(
            @RequestParam(value = "verbose", defaultValue = "false") boolean verbose) {
        try {

            EnigmaStatusDTO status = machineManager.getMachineStatus();
            return ResponseEntity.ok(status);
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
