package patmal.course.enigma.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import patmal.course.enigma.entity.MachineRotorEntity;

import java.util.UUID;

@Repository
public interface RotorsRepository extends JpaRepository<MachineRotorEntity, UUID> {
}
