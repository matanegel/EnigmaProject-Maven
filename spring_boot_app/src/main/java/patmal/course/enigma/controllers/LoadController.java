package patmal.course.enigma.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import patmal.course.enigma.storage.StorageManager;

@RestController
@RequestMapping("/enigma/load")
public class LoadController {

    private final StorageManager storageManager;

    @Autowired
    public LoadController(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    @PostMapping()
    public ResponseEntity<String> loadMachine(@RequestParam("file") MultipartFile file) {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty. Please upload a valid XML.");
        }


        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".xml")) {
            return ResponseEntity.badRequest().body("Only XML files are allowed.");
        }

        try {
            storageManager.loadSupplyFromStream(file.getInputStream());

            return ResponseEntity.ok("Enigma machine loaded successfully with " +
                    storageManager.getRotorsAmount() + " rotors.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error loading XML: " + e.getMessage());
        }
    }
}