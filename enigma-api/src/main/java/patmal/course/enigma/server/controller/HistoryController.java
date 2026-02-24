package patmal.course.enigma.server.controller;

import history.ConfigurationStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import patmal.course.enigma.server.runtime.EnigmaRunTime;

import java.util.List;

@RestController
public class HistoryController extends EnigmaController {


    public HistoryController(EnigmaRunTime enigmaRunTime) {
        super(enigmaRunTime);
    }

    @GetMapping("/history")
    public ResponseEntity<List<ConfigurationStats>> showHistory(){
        List<ConfigurationStats> history = this.getEnigmaRunTime().order7ShowHistory();
        return ResponseEntity.ok(history);
    }


}