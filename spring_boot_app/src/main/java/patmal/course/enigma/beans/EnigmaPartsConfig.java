package patmal.course.enigma.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import patmal.course.enigma.jaxb.EnigmaJaxbLoader;
import patmal.course.enigma.storage.StorageManager;

@Configuration
public class EnigmaPartsConfig {

    @Bean
    public StorageManager storageManager(EnigmaJaxbLoader enigmaJaxbLoader) {
        return new StorageManager(enigmaJaxbLoader);
    }

}
