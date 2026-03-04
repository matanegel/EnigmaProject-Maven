package patmal.course.enigma.server.controller;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import patmal.course.enigma.server.runtime.EnigmaRunTime;

@Getter
@RestController
@RequestMapping("/enigma")
public class EnigmaController {
    private EnigmaRunTime enigmaRunTime;

    @Autowired
    public EnigmaController(EnigmaRunTime enigmaRunTime) {
        this.enigmaRunTime = enigmaRunTime;
    }


}
