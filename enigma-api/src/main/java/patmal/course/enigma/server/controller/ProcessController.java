package patmal.course.enigma.server.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import patmal.course.enigma.server.dto.EncryptionOutputDTO;
import patmal.course.enigma.server.runtime.EnigmaRunTime;

@RestController
public class ProcessController extends EnigmaController {


    public ProcessController(EnigmaRunTime enigmaRunTime) {
        super(enigmaRunTime);
    }

    @PostMapping("/process")
    public ResponseEntity<EncryptionOutputDTO> encryptString(
            @RequestParam(name = "input") String input,
            @RequestParam(name = "sessionID") String sessionID) {
       input = input.trim().toUpperCase();
       ResponseEntity<EncryptionOutputDTO> response = this.getEnigmaRunTime().order5EncryptString(input, sessionID);
       return response;
    }

}
