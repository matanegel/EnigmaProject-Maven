package patmal.course.enigma.server.runtime;


import jaxb.EnigmaJaxbLoader;
import jaxb.config.EnigmaConfig;
import lombok.Getter;
import lombok.Setter;
import machine.Machine;
import org.springframework.stereotype.Service;
import patmal.course.enigma.server.dto.EnigmaStatusDTO;
import software.MachineConfig;
import storage.StorageManager;

import java.io.InputStream;

@Service
@Getter
@Setter
public class EnigmaRunTime {
    private StorageManager storageManager;
    private Machine machine;
    private CodeBuilder codeBuilder;

    private Boolean supplyLoaded = false;
    private Boolean machineConfigured = false;

    public EnigmaRunTime(EnigmaJaxbLoader loader, Machine machine) {
        this.storageManager = new StorageManager(loader);
        this.machine = machine;
        this.codeBuilder = new CodeBuilder(machine, storageManager);
    }

    public void order1LoadSupply(InputStream xmlStream) throws Exception {
        storageManager.loadSupplyFromStream(xmlStream);
        supplyLoaded = true;
    }



    public EnigmaStatusDTO createStatusDTO(Boolean verbose) {
        EnigmaStatusDTO.EnigmaStatusDTOBuilder builder = EnigmaStatusDTO.builder()
                .rotorsAmount(storageManager.getRotorsAmount())
                .reflectorsAmount(storageManager.getReflectorsAmount())
                .encryptionCount(machine.getEngine() != null ? machine.getEngine().getNumberOfEncryptions() : 0)
                .originalPositions(codeBuilder.getCode(true))
                .currentCode(codeBuilder.getCode(false));

        if (verbose) {
            // Here you would call the detailed logic we planned
            builder.originalCodeVerbose(codeBuilder.buildDetailedConfig(true))
                    .currentCodeVerbose(codeBuilder.buildDetailedConfig(false));
        }

        return builder.build();
    }

}
