package patmal.course.enigma.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import patmal.course.enigma.server.dto.SessionDTOInput;

import patmal.course.enigma.server.dto.SessionDTOOutput;
import patmal.course.enigma.server.runtime.EnigmaRunTime;

import java.util.UUID;

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

        String sessionID = enigmaRunTime
                .getSessionsManager().createSession(input.getMachineName());
        SessionDTOOutput response = new SessionDTOOutput();
        response.setSessionID(sessionID);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteSession(@RequestParam("sessionID") String sessionID) {
        if (sessionID == null) {
            return ResponseEntity.badRequest().body("Session ID is required");
        }
        UUID sessionUuid = UUID.fromString(sessionID);
        String result = enigmaRunTime.getSessionsManager().DeleteSession(sessionUuid);
        return ResponseEntity.ok(result);
    }
}
