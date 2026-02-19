package patmal.course.enigma.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import patmal.course.enigma.server.dto.SessionDTOInput;

import patmal.course.enigma.server.dto.SessionDTOOutput;
import patmal.course.enigma.server.runtime.EnigmaRunTime;

@Controller
@RequestMapping("/session")
public class SessionController {

    private EnigmaRunTime enigmaRunTime;

    public SessionController(EnigmaRunTime enigmaRunTime) {
        this.enigmaRunTime = enigmaRunTime;
    }

    @PostMapping
    public ResponseEntity<SessionDTOOutput> createSession(@RequestBody SessionDTOInput input) {

        if (input == null || input.getMachineName() == null) {
            return ResponseEntity.badRequest().build();
        }

        String sessionID = enigmaRunTime.createSession(input.getMachineName());

        SessionDTOOutput response = new SessionDTOOutput();
        response.setSessionID(sessionID);

        return ResponseEntity.ok(response);
    }
}
