package patmal.course.enigma;


import jakarta.persistence.EntityNotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import machine.Machine;
import patmal.course.enigma.entity.MachineEntity;
import patmal.course.enigma.mapper.MapperHolder;
import patmal.course.enigma.postgres.RepositoryHolder;
import storage.StorageManager;


import java.util.HashMap;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class SessionsManager {

    private final MapperHolder mapperHolder;
    private final RepositoryHolder repositoryHolder;
    private final HashMap<UUID, Machine> sessionIdToMachine = new HashMap<>();
    private final HashMap<UUID, StorageManager> sessionIdToStorogeManager = new HashMap<>();


    public String createSession(String machineName) {
        MachineEntity machineEntity = getMachineByName(machineName);
        StorageManager storageManager = mapperHolder.getMachineMapper().toStorageManager(
                machineEntity,
                mapperHolder.getMachineRotorMapper(),     // Pass the MachineRotorMapper instance
                mapperHolder.getMachineReflectorMapper()  // Pass the MachineReflectorMapper instance
        );

        Machine currentMachine = mapperHolder.getMachineMapper().entityToMachine(machineEntity);
        UUID sessionId = UUID.randomUUID();
        sessionIdToMachine.put(sessionId, currentMachine);
        sessionIdToStorogeManager.put(sessionId, storageManager);
        return sessionId.toString();
    }

    public StorageManager getStorageManagerBySessionId(String sessionId) {
        UUID uuid = UUID.fromString(sessionId);
        if (sessionIdToStorogeManager.containsKey(uuid)) {
            return sessionIdToStorogeManager.get(uuid);
        } else {
            throw new EntityNotFoundException("Unknown session id: " + sessionId);
        }
    }

    public Boolean DeleteSession(UUID sessionId) {
        if (sessionIdToMachine.containsKey(sessionId)) {
            sessionIdToMachine.remove(sessionId);
            return true;
        } else {
            return false;
        }
    }

    public Machine getMachineBySessionId(String sessionId) {
        UUID uuid = UUID.fromString(sessionId);
        if (sessionIdToMachine.containsKey(uuid)) {
            return sessionIdToMachine.get(uuid);
        } else {
            throw new EntityNotFoundException("Unknown session id: " + sessionId);
        }
    }

    public MachineEntity getMachineByName(String name) {
        return repositoryHolder.getMachineRepository().findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Unknown machine name: " + name));
    }
}