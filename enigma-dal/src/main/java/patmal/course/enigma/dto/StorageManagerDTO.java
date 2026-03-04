package patmal.course.enigma.dto;

import jaxb.EnigmaConfigMapper;
import jaxb.EnigmaJaxbLoader;
import jaxb.config.EnigmaConfig;
import lombok.Data;
import storage.PartsConfigValidator;
import storage.reflector.ReflectorStorage;
import storage.rotor.RotorStorage;

import java.util.List;

@Data
public class StorageManagerDTO {
    private String machineName;
    private RotorStorageDTO RS;
    private ReflectorStorageDTO RFS;
    private String ABC;
    private int rotorsCount;
}
