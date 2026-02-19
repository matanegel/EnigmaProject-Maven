package patmal.course.enigma.postgres;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import patmal.course.enigma.entity.MachineEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MachineRepository extends JpaRepository<MachineEntity, UUID> {
    Optional<MachineEntity> findByName(String name);
}
