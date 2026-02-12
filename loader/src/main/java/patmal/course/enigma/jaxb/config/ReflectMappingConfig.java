package patmal.course.enigma.jaxb.config;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class ReflectMappingConfig {

    @XmlAttribute(name = "input")
    private int input;

    @XmlAttribute(name = "output")
    private int output;

    public ReflectMappingConfig() {}

    public int getInput() {
        return input;
    }

    public int getOutput() {
        return output;
    }

    public void setInput(int input) {
        this.input = input;
    }

    public void setOutput(int output) {
        this.output = output;
    }

    public boolean validWiring(int minPort, int maxPort) {
        if (input < minPort || input > maxPort) {
            return false;
        }
        if (output < minPort || output > maxPort) {
            return false;
        }
        return input != output;
    }
}
