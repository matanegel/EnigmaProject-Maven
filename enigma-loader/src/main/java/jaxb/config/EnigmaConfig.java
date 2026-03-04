package jaxb.config;

import jakarta.xml.bind.annotation.*;


import java.util.List;

@XmlRootElement(name = "BTE-Enigma")
@XmlAccessorType(XmlAccessType.FIELD)
public class EnigmaConfig {

    @XmlAttribute(name = "name")
    private String name;

    @XmlAttribute(name = "rotors-count")
    private int rotorsCount;

    @XmlElement(name = "ABC")
    private String alphabet;

    @XmlElementWrapper(name = "BTE-Rotors")
    @XmlElement(name = "BTE-Rotor")
    private List<RotorConfig> rotors;

    @XmlElementWrapper(name = "BTE-Reflectors")
    @XmlElement(name = "BTE-Reflector")
    private List<ReflectorConfig> reflectors;

    // JAXB needs a no-arg constructor
    public EnigmaConfig() {}

    public int getRotorsCount() {
        return rotorsCount;
    }

    public void setRotorsCount(int rotorsCount) {
        this.rotorsCount = rotorsCount;
    }

    public String getAlphabet() {
        return alphabet != null ? alphabet.trim() : null;
    }

    public void setAlphabet(String alphabet) {
        this.alphabet = alphabet;
    }

    public List<RotorConfig> getRotors() {
        if (rotors.size() < 3) {
            throw new IllegalArgumentException("The number of rotors must be at least 3");
        }
        return rotors;
    }

    public void setRotors(List<RotorConfig> rotors) {
        this.rotors = rotors;
    }

    public List<ReflectorConfig> getReflectors() {
        return reflectors;
    }

    public void setReflectors(List<ReflectorConfig> reflectors) {
        this.reflectors = reflectors;
    }

    public boolean validateWires() {

           for (RotorConfig rotor : rotors) {
               try {

                   if (!rotor.rotorWiringisValid(alphabet)) {
                       return false;
                   }
               }
               catch (IllegalArgumentException e) {
                   throw new IllegalArgumentException("in rotor " + rotor.getID() + " - " + e.getMessage());
               }
           }

        for (ReflectorConfig reflector : reflectors) {
            try {
                if (!reflector.reflectorWiringisValid()) {
                    return false;
                }
            }
            catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("in reflector " + reflector.getId() + " - " + e.getMessage());
            }
        }
        return true;
    }

    public String getName() {
        return name;
    }
}

