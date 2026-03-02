package patmal.course.enigma.server.controller;

import history.ConfigurationStats;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import patmal.course.enigma.server.runtime.EnigmaRunTime;

import java.util.List;

@RestController
public class HistoryController extends EnigmaController {


    public HistoryController(EnigmaRunTime enigmaRunTime) {
        super(enigmaRunTime);
    }

    @GetMapping("/history")
    public ResponseEntity<List<ConfigurationStats>> showHistory(
            @RequestParam(name = "sessionID", required = false) String sessionID,
            @RequestParam(name = "machineName", required = false) String machineName) {
        // Logical XOR: true only if exactly one is non-null
        if ((sessionID == null) == (machineName == null)) {
            throw new IllegalArgumentException("Invalid request: Provide either 'sessionID' or 'machineName', but not both.");
        }
        List<ConfigurationStats> history;
        if (sessionID != null) {
            history = this.getEnigmaRunTime().order7ShowHistoryBySessionID(sessionID);
        }
        else {
            history = this.getEnigmaRunTime().order7ShowHistoryByMachineName(machineName);
        }
        return ResponseEntity.ok(history);
    }


}