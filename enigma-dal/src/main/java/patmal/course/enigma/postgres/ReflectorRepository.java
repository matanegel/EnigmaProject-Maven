package patmal.course.enigma.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import patmal.course.enigma.entity.MachineReflectorEntity;


import java.util.UUID;

@Repository
public interface ReflectorRepository extends JpaRepository<MachineReflectorEntity, UUID> {
}