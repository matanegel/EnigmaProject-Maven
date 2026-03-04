package patmal.course.enigma.mapper;


import history.ConfigurationStats;
import history.ProcessedString;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import patmal.course.enigma.entity.ProcessingEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProcessingMapper {

    //  Map the Entity to the "ProcessedString" DTO
    @Mapping(target = "source", source = "input")
    @Mapping(target = "result", source = "output")
    @Mapping(target = "timeTaken", source = "time")
    ProcessedString toProcessedString(ProcessingEntity entity);

    //  Map a List of Entities to a List of ProcessedStrings
    List<ProcessedString> toProcessedStringList(List<ProcessingEntity> entities);

    default ConfigurationStats toConfigStats(String config, List<ProcessingEntity> entities, String sessionID) {
        ConfigurationStats stats = new ConfigurationStats(config);
        stats.setSessionID(sessionID);
        if (entities != null) {
            stats.setProcessedStrings(toProcessedStringList(entities));
        }
        return stats;
    }
}