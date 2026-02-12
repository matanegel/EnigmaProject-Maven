package jaxb.config;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jaxb.WiredPart;


import java.util.List;
import java.util.function.Function;

@XmlAccessorType(XmlAccessType.FIELD)
public class ReflectorConfig implements WiredPart {

    @XmlAttribute(name = "id")
    private String id;  // e.g. "I", "II"

    @XmlElement(name = "BTE-Reflect")
    private List<ReflectMappingConfig> mappings;

    public ReflectorConfig() {}

    public String getId() {
        return id;
    }

    public List<ReflectMappingConfig> getMappings() {
        return mappings;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMappings(List<ReflectMappingConfig> mappings) {
        this.mappings = mappings;
    }

    @Override
    public String getPartType() {
        return ReflectorConfig.class.getSimpleName();
    }

    @Override
    public int getID() {
        // Defining the Lambda
        Function<String, Integer> romanToInt = s -> switch (s) {
            case "I"   -> 1;
            case "II"  -> 2;
            case "III" -> 3;
            case "IV"  -> 4;
            case "V"   -> 5;
            default    -> -1;
        };
        return romanToInt.apply(this.id);
    }


    public List<ReflectMappingConfig> getWires() {
        return this.mappings;
    }

    public boolean reflectorWiringisValid() {
        List<ReflectMappingConfig> wires = getMappings();
        int minPort = 1;
        int maxPort = wires.size() * 2;

        // Check that each mapping is valid and that no input or output is repeated
        boolean[] usedInputs = new boolean[maxPort - minPort + 1];
        boolean[] usedOutputs = new boolean[maxPort - minPort + 1];

        for (ReflectMappingConfig mapping : wires) {
            int input = mapping.getInput();
            int output = mapping.getOutput();

            if (!mapping.validWiring(minPort, maxPort)) {
                return false;
            }

            if (usedInputs[input - minPort] || usedOutputs[output - minPort]) {
                return false; // Duplicate input or output
            }

            usedInputs[input - minPort] = true;
            usedOutputs[output - minPort] = true;
        }
        return true;
    }


}
