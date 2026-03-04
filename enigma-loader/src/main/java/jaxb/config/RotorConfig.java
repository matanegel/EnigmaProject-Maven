package jaxb.config;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jaxb.WiredPart;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

@XmlAccessorType(XmlAccessType.FIELD)
public class RotorConfig implements WiredPart {

    @XmlAttribute(name = "id")
    private int id;

    @XmlAttribute(name = "notch")
    private int notch;

    @XmlElement(name = "BTE-Positioning")
    private List<PositioningConfig> positions;

    public RotorConfig() {}

    public int getId() {
        return id;
    }

    public int getNotch() {
        return notch;
    }

    public List<PositioningConfig> getPositions() {
        return positions;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNotch(int notch) {
        this.notch = notch;
    }

    public void setPositions(List<PositioningConfig> positions) {
        this.positions = positions;
    }

    @Override
    public String getPartType() {
        return ReflectorConfig.class.getSimpleName();
    }

    @Override
    public int getID() {
        return this.id;
    }


    public List<PositioningConfig> getWires() {
        return this.positions;
    }

    public boolean rotorWiringisValid(String ABC) {
        List<PositioningConfig> wires = getPositions();
        int minPort = 0;
        int maxPort = ABC.length() - 1;

        // Check that each positioning is valid and that no right or left value is repeated
        Set<Integer> usedReoutorInPorts = new HashSet<>();
        Set<Integer> usedReoutorOutPorts = new HashSet<>();

        for (PositioningConfig position : wires) {
            String right = position.getRight();
            String left = position.getLeft();

            if(!(ABC.contains(right)) || !(ABC.contains(left)) ) {
                throw new IllegalArgumentException("Invalid port found in rotor wiring: " + right + " or " + left);
            }
            if (usedReoutorInPorts.contains(ABC.indexOf(right)) || usedReoutorOutPorts.contains(ABC.indexOf(left))) {
                throw new IllegalArgumentException("Duplicate port found in rotor wiring: " + right + " or " + left);
            }
            usedReoutorInPorts.add(ABC.indexOf(right));
            usedReoutorOutPorts.add(ABC.indexOf(left));
        }
        return true;
    }
}
