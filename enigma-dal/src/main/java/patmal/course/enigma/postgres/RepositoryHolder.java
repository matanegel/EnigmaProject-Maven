package patmal.course.enigma.postgres;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
@Getter
public class RepositoryHolder {
    private final MachineRepository machineRepository;
    private final RotorsRepository rotorsRepository;
    private final ReflectorRepository reflectorRepository;;
}
