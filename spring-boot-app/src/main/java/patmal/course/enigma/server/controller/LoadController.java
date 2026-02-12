package patmal.course.enigma.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import patmal.course.enigma.server.runtime.EnigmaRunTime;

@RestController
@RequestMapping("/load")
public class LoadController {
    private EnigmaRunTime enigmaRunTime;

    @Autowired
    public LoadController(EnigmaRunTime enigmaRunTime) {
        this.enigmaRunTime = enigmaRunTime;
    }

    @PostMapping
    public ResponseEntity<String> loadMachine(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty. Please upload a valid XML.");
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".xml")) {
            return ResponseEntity.badRequest().body("Only XML files are allowed.");
        }

        try {
            enigmaRunTime.order1LoadSupply(file.getInputStream());
            return ResponseEntity.ok("Enigma machine loaded successfully with " +
                    enigmaRunTime.getStorageManager().getRotorsAmount() + " rotors.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error loading XML: " + e.getMessage());
        }
    }
}
