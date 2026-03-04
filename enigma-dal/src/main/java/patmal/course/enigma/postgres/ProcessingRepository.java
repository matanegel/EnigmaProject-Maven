package patmal.course.enigma.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import patmal.course.enigma.entity.MachineEntity;
import patmal.course.enigma.entity.ProcessingEntity;

import java.util.List;
import java.util.UUID;

public interface ProcessingRepository extends JpaRepository<ProcessingEntity, UUID> {
    List<ProcessingEntity> getProcessingEntitiesByMachineId (MachineEntity machineId);
}
