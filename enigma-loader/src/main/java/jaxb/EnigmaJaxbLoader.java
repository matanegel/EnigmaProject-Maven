package jaxb;


import jaxb.config.EnigmaConfig;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;

public class EnigmaJaxbLoader {

    // 1. PERFORMANCE: Cache the context so it is created only once.
    private static final JAXBContext CONTEXT;

    static {
        try {
            CONTEXT = JAXBContext.newInstance(EnigmaConfig.class);
        } catch (JAXBException e) {
            throw new RuntimeException("Failed to initialize JAXB Context for EnigmaConfig", e);
        }
    }

    /**
     * Loads an EnigmaConfig from an XML file path.
     */
    public EnigmaConfig loadFromFile(String filePath) throws JAXBException, FileNotFoundException {
        File file = new File(filePath);

        // 2. VALIDATION: Check if file exists and is actually a file
        if (!file.exists() || !file.isFile()) {
            throw new FileNotFoundException("File not found: " + filePath);
        }

        // 3. SAFETY: Check extension (Case-insensitive)
        if (!filePath.toLowerCase().endsWith(".xml")) {
            throw new IllegalArgumentException("File must be an XML type: " + filePath);
        }

        // Create Unmarshaller (Not thread-safe, so we create one per request)
        Unmarshaller unmarshaller = CONTEXT.createUnmarshaller();

        return (EnigmaConfig) unmarshaller.unmarshal(file);
    }
}
