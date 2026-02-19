package patmal.course.enigma;


import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import machine.Machine;
import patmal.course.enigma.entity.MachineEntity;
import patmal.course.enigma.mapper.MapperHolder;
import patmal.course.enigma.postgres.RepositoryHolder;


import java.util.HashMap;
import java.util.UUID;


@RequiredArgsConstructor
public class SessionsManager {

    private final MapperHolder mapperHolder;
    private final RepositoryHolder repositoryHolder;
    private final HashMap<UUID, Machine> sessions = new HashMap<>();



    public String createSession(String machineName) {
            MachineEntity machineEntity = getMachineById(machineName);
            Machine currentMachine = mapperHolder.getMachineMapper().entityToMachine(machineEntity);
            UUID sessionId = UUID.randomUUID();
            sessions.put(sessionId, currentMachine);
            return sessionId.toString();
    }


    public MachineEntity getMachineById(String name) {
        return repositoryHolder.getMachineRepository().findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Machine with name " + name + " not found"));
    }
}