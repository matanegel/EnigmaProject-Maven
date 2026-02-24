package patmal.course.enigma.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import patmal.course.enigma.server.dto.SessionDTOInput;

import patmal.course.enigma.server.dto.SessionDTOOutput;
import patmal.course.enigma.server.runtime.EnigmaRunTime;

import java.util.UUID;

@Controller
public class SessionController extends EnigmaController {


    public SessionController(EnigmaRunTime enigmaRunTime) {
        super(enigmaRunTime);
    }

    @PostMapping("/session")
    public ResponseEntity<SessionDTOOutput> createSession(@RequestBody SessionDTOInput input) {

        if (input == null || input.getMachineName() == null) {
            return ResponseEntity.badRequest().build();
        }

        String sessionID = this.getEnigmaRunTime()
                .getSessionsManager().createSession(input.getMachineName());
        SessionDTOOutput response = new SessionDTOOutput();
        response.setSessionID(sessionID);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/session")
    public ResponseEntity<String> deleteSession(@RequestParam("sessionID") String sessionID) {
        if (sessionID == null) {
            return ResponseEntity.badRequest().body("Session ID is required");
        }
        UUID sessionUuid = UUID.fromString(sessionID);
        if(this.getEnigmaRunTime().getSessionsManager().DeleteSession(sessionUuid)){;
            return ResponseEntity.ok("Session deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Unknown session ID: " + sessionID);
        }
    }
}
