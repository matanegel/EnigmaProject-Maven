package patmal.course.enigma.jaxb.config;


import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class PositioningConfig {

    @XmlAttribute(name = "right")
    private String right;

    @XmlAttribute(name = "left")
    private String left;

    public PositioningConfig() {}

    public String getRight() {
        return right;
    }

    public String getLeft() {
        return left;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public boolean validWiring(int alphabetSize) {
        if (right.length() != alphabetSize) {
            return false;
        }
        return left.length() == alphabetSize;
    }
}
