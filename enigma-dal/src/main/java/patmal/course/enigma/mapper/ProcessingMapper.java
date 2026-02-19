//package patmal.course.enigma.mapper;
//
//import hardware.parts.Rotor;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//
//import patmal.course.enigma.entity.ProcessingEntity;
//
//
//@Mapper
//public interface ProcessingMapper {
//
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "machineId", ignore = true)
//    @Mapping(target = "sessionId", source = "sessionId")
//    @Mapping(target = "code", source = "code")
//    @Mapping(target = "input", source = "input")
//    @Mapping(target = "output", source = "output")
//    @Mapping(target = "time", source = "time")
//    //ProcessingEntity toEntity(ProcessingDTO processingDTO);
//}
