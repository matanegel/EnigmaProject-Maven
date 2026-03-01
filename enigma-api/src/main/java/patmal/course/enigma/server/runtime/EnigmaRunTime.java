package patmal.course.enigma.server.runtime;


import hardware.engine.Engine;
import hardware.engine.RotorsManagers;
import hardware.parts.Plugboard;
import hardware.parts.Reflector;
import hardware.parts.Rotor;
import history.ConfigurationStats;
import jakarta.persistence.EntityNotFoundException;
import jaxb.EnigmaJaxbLoader;
import lombok.Getter;
import lombok.Setter;
import machine.Machine;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import patmal.course.enigma.SessionsManager;
import patmal.course.enigma.entity.MachineEntity;
import patmal.course.enigma.entity.MachineReflectorEntity;
import patmal.course.enigma.entity.MachineRotorEntity;
import patmal.course.enigma.mapper.MapperHolder;
import patmal.course.enigma.postgres.RepositoryHolder;
import patmal.course.enigma.server.dto.EncryptionOutputDTO;
import patmal.course.enigma.server.dto.EnigmaManualConfigDTO;
import patmal.course.enigma.server.dto.EnigmaStatusDTO;

import software.AutoConfig;
import software.MachineConfig;
import storage.StorageManager;

import java.io.InputStream;
import java.util.*;

@Service
@Getter
@Setter
public class EnigmaRunTime {
    private StorageManager storageManager;
    private SessionsManager sessionsManager;
    private MachineBySession machineBySession;
    private EnigmaJaxbLoader loader;

    //DB repository
    private final RepositoryHolder repositoryHolder;
    private final MapperHolder mapperHolder;

    private Boolean supplyLoaded = false;
    private Boolean machineConfigured = false;

    @Setter
    @Getter
    private static class MachineBySession {
        private UUID sessionId;
        private StorageManager sessionStorageManager;
        private Machine machine;
        private CodeBuilder codeBuilder;

        public MachineBySession(UUID sessionId, Machine machine, StorageManager storageManager) {
            this.sessionId = sessionId;
            this.sessionStorageManager = storageManager;
            this.machine = machine;
            this.codeBuilder = new CodeBuilder(machine, storageManager);
        }
    }

    public EnigmaRunTime(EnigmaJaxbLoader loader, RepositoryHolder repositoryHolder, MapperHolder mapperHolder) {
        this.storageManager = new StorageManager(loader);
        this.loader = loader;
        this.repositoryHolder = repositoryHolder;
        this.mapperHolder = mapperHolder;
        this.sessionsManager = new SessionsManager(mapperHolder, repositoryHolder);
    }

    public void buildMachineBySessionId(String sessionId) {
        UUID uuid = UUID.fromString(sessionId);
        if (sessionsManager.getSessionIdToMachine().containsKey(uuid)) {



            Machine machine = sessionsManager.getSessionIdToMachine().get(uuid);
            StorageManager storageManager = sessionsManager.getSessionIdToStorogeManager().get(uuid);
            this.machineBySession = new MachineBySession(uuid, machine, storageManager);
        } else {
            throw new EntityNotFoundException("Unknown session id: " + sessionId);
        }
    }

    public void order1LoadSupply(InputStream xmlStream) throws Exception {
        try {
            storageManager.resetUsedIds();
            StorageManager tempSM = new StorageManager(loader);
            tempSM.loadSupplyFromStream(xmlStream);
            storageManager = tempSM;

            this.machineBySession = new MachineBySession(null, new Machine(), storageManager);
            supplyLoaded = true;

            MachineEntity machineEntity = createBaseMachineEntity();

            Boolean machineExists = repositoryHolder
                    .getMachineRepository()
                    .existsByName(machineEntity.getName());

            if(machineExists) {
                throw new IllegalStateException("Machine with name " + machineEntity.getName() + " already exists in the database");
            }

            machineEntity.setRotors(createRotorEntities(machineEntity));
            machineEntity.setReflectors(createReflectorEntities(machineEntity));
            repositoryHolder.getMachineRepository().save(machineEntity);
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
                .totalProcessedMessages(this.machineBySession.getMachine().getEngine() != null ? this.machineBySession.getMachine().getEngine().getNumberOfEncryptions() : 0)
                .originalCodeCompact(this.machineBySession.getCodeBuilder().getCode(true))
                .currentRotorsPositionCompact(this.machineBySession.getCodeBuilder().getCode(false));

        if (verbose) {
            // Here you would call the detailed logic we planned
            builder.originalCode(this.machineBySession.getCodeBuilder().buildDetailedConfig(true))
                    .currentRotorsPosition(this.machineBySession.getCodeBuilder().buildDetailedConfig(false));
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
        RotorsManagers manager = new RotorsManagers(rotors.toArray(new Rotor[0]));


        storageManager.setOriginalPosition(positions);

        List<Integer> indexOfPositions = manager.MappingInputCharPositionByRightColumnToIndex(positions);
        manager.setRotorsPosition(indexOfPositions);

        this.machineBySession.getMachine().setEngine(new Engine(rotorsCount, reflector, manager, plugBoard, storageManager.getABC()));


        String initCode = this.machineBySession.getCodeBuilder().getCode(true);
        ConfigurationStats state = new ConfigurationStats(initCode);
        this.machineBySession.getMachine().getFullHistory().add(state);

        return "Manual code set successfully";
    }

    public String order4GetAutomaticConfig() {
        if (!supplyLoaded) {
            throw new UnsupportedOperationException("XML File Not Loaded Yet - Make Order 1 First");
        }
        MachineConfig machineConfiguration = new AutoConfig(storageManager);
        Engine newEngine = machineConfiguration.configureAndGetEngine();
        machineBySession.getMachine().setEngine(newEngine);
        String initCode = machineBySession.getCodeBuilder().getCode(true);
        ConfigurationStats state = new ConfigurationStats(initCode);
        this.machineBySession.getMachine().getFullHistory().add(state);
        return "Automatic code setup completed successfully";
    }

    public ResponseEntity<EncryptionOutputDTO> order5EncryptString(String input) {
        if (this.machineBySession.getMachine().getEngine() == null) {
            throw new UnsupportedOperationException("Engine Not Configured Yet - Make Order 3/4 First");
        }


        long start = System.nanoTime();

        patmal.course.enigma.server.dto.EncryptionOutputDTO answer = new EncryptionOutputDTO();

        String cipher = this.machineBySession.getMachine().getEngine().processString(input);


        answer.setOutput(cipher);
        answer.setCurrentRotorsPositionCompact(this.machineBySession.getCodeBuilder().buildPositionString(false));
        // end measure time
        long end = System.nanoTime();


        if (!this.machineBySession.getMachine().getFullHistory().isEmpty()) {
            // pull the last configuration stats
            this.machineBySession.getMachine().getFullHistory().getLast().addProcessedString(input, cipher, (end - start));
        }
        return ResponseEntity.ok(answer);
    }

    public String order6RestartMachineConfig() {
        if (this.machineBySession.getMachine().getEngine() == null) {
            throw new UnsupportedOperationException("Engine Not Configured Yet - Make Order 3/4 First");
        }
        Rotor[] rotors = this.machineBySession.getMachine().getEngine().getRotorsManagers().getRotors();
        List<Character> originalPosition = storageManager.getOriginalPosition();
        for (int i = 0; i < rotors.length; i++) {
            rotors[i].setPosition(rotors[i].getWiringRotor().getIndexOfChInRightColumn(originalPosition.get(i)));
        }
        //String initCode = codeBuilder.getCode(true);
        return "Code rested completed successfully";
    }

    public List<ConfigurationStats> order7ShowHistory() {
        if (this.machineBySession.getMachine().getFullHistory().isEmpty()) {
            throw new UnsupportedOperationException ("\nNo history found. The machine hasn't been configured yet.");
        }

        List<ConfigurationStats> history = new ArrayList<>();

        history = this.machineBySession.getMachine().getFullHistory();
        return history;
    }

    private MachineEntity createBaseMachineEntity() {
        this.machineBySession.getMachine().setRotorsCount(storageManager.getRotorsAmount());
        this.machineBySession.getMachine().setAlphabet(storageManager.getABC());
        this.machineBySession.getMachine().setName(storageManager.getMachineName());
        this.machineBySession.getMachine().setId(UUID.randomUUID());
        return mapperHolder.getMachineMapper().machineToEntity(this.machineBySession.getMachine());
    }

    private List<MachineRotorEntity> createRotorEntities(MachineEntity parent) {
        return storageManager.getRS().getRotorMap().values().stream()
                .map(rotor -> {
                    MachineRotorEntity entity = mapperHolder.getMachineRotorMapper().toEntity(rotor);
                    entity.setMachineId(parent);
                    return entity;
                })
                .toList();
    }

    private List<MachineReflectorEntity> createReflectorEntities(MachineEntity parent) {
        return storageManager.getRFS().getReflectorMap().values().stream()
                .map(reflector -> {
                    MachineReflectorEntity entity = mapperHolder.getMachineReflectorMapper().toEntity(reflector);
                    entity.setMachineId(parent);
                    return entity;
                })
                .toList();
    }

    public Machine getMachine() {
        return this.machineBySession.getMachine();
    }
}
