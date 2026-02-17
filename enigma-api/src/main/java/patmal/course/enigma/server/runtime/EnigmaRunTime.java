package patmal.course.enigma.server.runtime;


import dto.db.MachineDTO;
import hardware.engine.Engine;
import hardware.engine.rotorsManagers;
import hardware.parts.Plugboard;
import hardware.parts.Reflector;
import hardware.parts.Rotor;
import history.ConfigurationStats;
import jaxb.EnigmaJaxbLoader;
import lombok.Getter;
import lombok.Setter;
import machine.Machine;
import mapper.MachineMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import patmal.course.enigma.entity.MachineEntity;
import patmal.course.enigma.postgres.MachineRepository;
import patmal.course.enigma.server.dto.EncryptionOutputDTO;
import patmal.course.enigma.server.dto.EnigmaManualConfigDTO;
import patmal.course.enigma.server.dto.EnigmaStatusDTO;
import patmal.course.enigma.server.mapper.MachineMapperApi;
import software.AutoConfig;
import software.MachineConfig;
import storage.StorageManager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static mapper.MachineMapper.toDTO;

@Service
@Getter
@Setter
public class EnigmaRunTime {
    private StorageManager storageManager;
    private Machine machine;
    private CodeBuilder codeBuilder;
    private EnigmaJaxbLoader loader;

    //DB repository
    private final MachineRepository machineRepository;
    private final MachineMapperApi machineMapper;

    private Boolean supplyLoaded = false;
    private Boolean machineConfigured = false;

    public EnigmaRunTime(EnigmaJaxbLoader loader, Machine machine, MachineRepository machineRepository, MachineMapperApi machineMapper) {
        this.storageManager = new StorageManager(loader);
        this.machine = machine;
        this.codeBuilder = new CodeBuilder(machine, storageManager);
        this.loader = loader;
        this.machineRepository = machineRepository;
        this.machineMapper = machineMapper;
    }

    public void order1LoadSupply(InputStream xmlStream) throws Exception {
        try {
            storageManager.resetUsedIds();
            StorageManager tempSM = new StorageManager(loader);
            tempSM.loadSupplyFromStream(xmlStream);
            storageManager = tempSM;
            this.codeBuilder.setSM(storageManager);


            supplyLoaded = true;
            machine.setRotorsCount(storageManager.getRotorsAmount());
            machine.setAlphabet(storageManager.getABC());
            machine.setName(storageManager.getMachineName());
            MachineEntity entity = machineMapper.machineToEntity(machine);
            machineRepository.save(entity);
        }
        catch (IllegalArgumentException iae) {
            throw new Exception("Invalid XML file: " + iae.getMessage());
        } catch (Exception e) {
            throw new Exception("Failed to load XML file: " + e.getMessage());
        }
    }

    public EnigmaStatusDTO order2CreateStatusDTO(Boolean verbose) {
        EnigmaStatusDTO.EnigmaStatusDTOBuilder builder = EnigmaStatusDTO.builder()
                .totalRotors(storageManager.getRotorsAmount())
                .totalReflectors(storageManager.getReflectorsAmount())
                .totalProcessedMessages(machine.getEngine() != null ? machine.getEngine().getNumberOfEncryptions() : 0)
                .originalCodeCompact(codeBuilder.getCode(true))
                .currentRotorsPositionCompact(codeBuilder.getCode(false));

        if (verbose) {
            // Here you would call the detailed logic we planned
            builder.originalCode(codeBuilder.buildDetailedConfig(true))
                    .currentRotorsPosition(codeBuilder.buildDetailedConfig(false));
        }

        return builder.build();
    }

    public String order3GetManualConfig(EnigmaManualConfigDTO manualConfig) {
        List<Character> positions = new ArrayList<>();
        Set<Integer> usedRotors = new HashSet<>();
        List<Rotor> rotors = new ArrayList<>();
        Plugboard plugBoard = new Plugboard(storageManager.getABC());
        if (!supplyLoaded) {
            throw new UnsupportedOperationException("XML File Not Loaded Yet - Make Order 1 First");
        }


        for (EnigmaManualConfigDTO.RotorSelectionDTO rotorSelection : manualConfig.getRotors()) {
            int rotorId = rotorSelection.getRotorNumber();
            if (usedRotors.contains(rotorId)) {
                throw new IllegalStateException("Cannot build enigma machine with duplicate rotors");
            }
            usedRotors.add(rotorId);
            rotors.add(storageManager.optionalGetRotorByID(rotorId));
            if (rotorSelection.getRotorPosition() != null && !rotorSelection.getRotorPosition().isEmpty()) {

                positions.add(Character.toUpperCase(rotorSelection.getRotorPosition().charAt(0)));
            }
        }
        rotors = new ArrayList<>(rotors.reversed());

        if (rotors.size() != positions.size()) {
            throw new IllegalArgumentException("Number of positions must match rotors amount");
        }

        positions = positions.reversed();
        storageManager.setOriginalPosition(positions);
        Reflector reflector = storageManager.optionalGetReflectorByID(manualConfig.getReflector());


        try {
            if (manualConfig.getPlugs() != null && !manualConfig.getPlugs().isEmpty()) {
                StringBuilder plugStringBuilder = new StringBuilder();
                for (EnigmaManualConfigDTO.PlugPairDTO plugPair : manualConfig.getPlugs()) {
                    plugStringBuilder.append(plugPair.getPlug1());
                    plugStringBuilder.append(plugPair.getPlug2());
                }

                String pipeFormat = Plugboard.parsePairFormat(plugStringBuilder.toString());
                plugBoard = new Plugboard(storageManager.getABC(), pipeFormat);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid plugboard configuration: " + e.getMessage());
        }


        int rotorsCount = storageManager.getRotorsCount();
        rotorsManagers manager = new rotorsManagers(rotors.toArray(new Rotor[0]));


        storageManager.setOriginalPosition(positions);

        List<Integer> indexOfPositions = manager.MappingInputCharPositionByRightColumnToIndex(positions);
        manager.setRotorsPosition(indexOfPositions);

        machine.setEngine(new Engine(rotorsCount, reflector, manager, plugBoard, storageManager.getABC()));


        String initCode = codeBuilder.getCode(true);
        ConfigurationStats state = new ConfigurationStats(initCode);
        machine.getFullHistory().add(state);

        return "Manual code set successfully";
    }

    public String order4GetAutomaticConfig() {
        if (!supplyLoaded) {
            throw new UnsupportedOperationException("XML File Not Loaded Yet - Make Order 1 First");
        }
        MachineConfig machineConfiguration = new AutoConfig(storageManager);
        Engine newEngine = machineConfiguration.configureAndGetEngine();
        machine.setEngine(newEngine);
        String initCode = codeBuilder.getCode(true);
        ConfigurationStats state = new ConfigurationStats(initCode);
        machine.getFullHistory().add(state);
        return "Automatic code setup completed successfully";
    }

    public ResponseEntity<EncryptionOutputDTO> order5EncryptString(String input) {
        if (machine.getEngine() == null) {
            throw new UnsupportedOperationException("Engine Not Configured Yet - Make Order 3/4 First");
        }


        long start = System.nanoTime();

        patmal.course.enigma.server.dto.EncryptionOutputDTO answer = new EncryptionOutputDTO();

        String cipher = machine.getEngine().processString(input);


        answer.setOutput(cipher);
        answer.setCurrentRotorsPositionCompact(codeBuilder.buildPositionString(false));
        // end measure time
        long end = System.nanoTime();


        if (!machine.getFullHistory().isEmpty()) {
            // pull the last configuration stats
            machine.getFullHistory().getLast().addProcessedString(input, cipher, (end - start));
        }
        return ResponseEntity.ok(answer);
    }

    public String order6RestartMachineConfig() {
        if (machine.getEngine() == null) {
            throw new UnsupportedOperationException("Engine Not Configured Yet - Make Order 3/4 First");
        }
        Rotor[] rotors = machine.getEngine().getRotorsManagers().getRotors();
        List<Character> originalPosition = storageManager.getOriginalPosition();
        for (int i = 0; i < rotors.length; i++) {
            rotors[i].setPosition(rotors[i].getWiringRotor().getIndexOfChInRightColumn(originalPosition.get(i)));
        }
        //String initCode = codeBuilder.getCode(true);
        return "Code rested completed successfully";
    }

    public List<ConfigurationStats> order7ShowHistory() {
        if (machine.getFullHistory().isEmpty()) {
            throw new UnsupportedOperationException ("\nNo history found. The machine hasn't been configured yet.");
        }

        List<ConfigurationStats> history = new ArrayList<>();

        history = machine.getFullHistory();
        return history;
    }
}
