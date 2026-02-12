package patmal.course.enigma.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import patmal.course.enigma.server.dto.EnigmaStatusDTO;
import patmal.course.enigma.server.runtime.EnigmaRunTime;

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



}
