package patmal.course.enigma.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import patmal.course.enigma.server.runtime.EnigmaRunTime;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoadController extends EnigmaController {

    public LoadController(EnigmaRunTime enigmaRunTime) {
        super(enigmaRunTime);
    }

    @PostMapping("/load")
    public ResponseEntity<Map<String, Object>> loadMachine(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();

        // 1. Validation
        if (file.isEmpty()) {
            response.put("success", false);
            response.put("error", "File not provided");
            return ResponseEntity.badRequest().body(response);
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".xml")) {
            response.put("success", false);
            response.put("error", "Only XML files are allowed.");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // 2. Business Logic
            this.getEnigmaRunTime().order1LoadSupply(file.getInputStream());

            // 3. Success Response matching the Doc
            response.put("success", true);
            response.put("name", "Enigma-M1"); // You might want to get this from your engine
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // 4. Error Response matching the Doc
            response.put("success", false);
            response.put("error", "Invalid XML structure");
            // Returning 200 here matches the 'Example Value' in your Swagger screenshot
            return ResponseEntity.ok(response);
        }
    }
}
