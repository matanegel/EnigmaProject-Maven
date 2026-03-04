package patmal.course.enigma.mapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Getter
@RequiredArgsConstructor
public class MapperHolder {
    private final MachineMapperApi machineMapper;
    private final MachineRotorMapper machineRotorMapper;
    private final MachineReflectorMapper machineReflectorMapper;
    private final ProcessingMapper processingMapper;
}
