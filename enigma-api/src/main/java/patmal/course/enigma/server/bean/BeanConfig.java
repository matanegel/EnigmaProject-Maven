package patmal.course.enigma.server.bean;

import jaxb.EnigmaJaxbLoader;
import machine.Machine;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import patmal.course.enigma.server.runtime.CodeBuilder;

@Component
public class BeanConfig {

    @Bean
    public EnigmaJaxbLoader enigmaJaxbLoader() {
        return new EnigmaJaxbLoader();
    }



    @Bean
    public Machine machine() {
        return new Machine();
    }

}
