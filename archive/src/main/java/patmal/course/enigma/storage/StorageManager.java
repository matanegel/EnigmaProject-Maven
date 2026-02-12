package patmal.course.enigma.storage;



import hardware.Utils;
import hardware.parts.Reflector;
import hardware.parts.Rotor;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import patmal.course.enigma.jaxb.EnigmaConfigMapper;
import patmal.course.enigma.jaxb.EnigmaJaxbLoader;
import patmal.course.enigma.jaxb.config.EnigmaConfig;
import patmal.course.enigma.jaxb.config.ReflectorConfig;
import patmal.course.enigma.jaxb.config.RotorConfig;
import patmal.course.enigma.storage.reflector.ReflectorStorage;
import patmal.course.enigma.storage.rotor.RotorStorage;

import java.io.InputStream;
import java.util.List;

public class StorageManager implements software.StorageProvider {
    private final EnigmaJaxbLoader supplyLoader;
    private EnigmaConfig EC;
    private EnigmaConfigMapper ECM;
    private PartsConfigValidator PCV;
    private RotorStorage RS;
    private ReflectorStorage RFS;
    private String ABC;
    private int rotorsCount;
    private List<Character>  originalPosition;
    private boolean ValidSupply = false;
    private StorageManager myCopy;

    public StorageManager(EnigmaJaxbLoader loader) {
       this.supplyLoader = loader;
    }

    private boolean validatePartsConfig() {
        if (EC.getRotors() != null) {
            for (RotorConfig rotorConfig : EC.getRotors()) {
                if (!PCV.runRotorValidation(rotorConfig)) {
                    return false;
                }
            }
        }

        if (EC.getReflectors() != null) {
            for (ReflectorConfig reflectorConfig : EC.getReflectors()) {
                if (!PCV.runReflectorValidation(reflectorConfig)) {
                    return false;
                }
            }
        }

        return PCV.rotorIdsHasNoHoles() && PCV.reflectorIdsHasNoHoles();
    }

    private boolean validateABCLength() {
        if (EC.getAlphabet() != null && Utils.normalize(ABC.length(), 2) != 0) {
            throw new IllegalArgumentException("Alphabet length must be even.");
        }
        return true;
    }

    private boolean validateSupply() {
        if (EC.getRotorsCount() > EC.getRotors().size()) {
            throw new IllegalArgumentException("Rotors count in config is larger than available rotors.");
        }
        if (EC.getRotorsCount() < 1) {
            throw new IllegalArgumentException("Rotors count in config must be at least 1.");
        }
        return validateABCLength() && validatePartsConfig() && EC.validateWires();
    }


    private void buildStorages() {
        try {
            RS = new RotorStorage(ECM.buildRotors());
            RFS = new ReflectorStorage(ECM.buildReflectors());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to build storages: " + e.getMessage());
        }
    }

    private void loadSupplyFromXML(String path) throws Exception {
        EC = supplyLoader.loadFromFile(path);
        ECM = new EnigmaConfigMapper(EC);
        PCV = new PartsConfigValidator();
        ABC = EC.getAlphabet();
        rotorsCount = EC.getRotorsCount();
    }

    public void loadSupplyFromStream(InputStream inputStream) throws Exception {
        // כאן אנחנו משתמשים ב-Unmarshaller ישירות על ה-Stream
        JAXBContext context = JAXBContext.newInstance(EnigmaConfig.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        this.EC = (EnigmaConfig) unmarshaller.unmarshal(inputStream);
        this.ECM = new EnigmaConfigMapper(EC);
        this.PCV = new PartsConfigValidator();
        this.ABC = EC.getAlphabet();
        this.rotorsCount = EC.getRotorsCount();

        // ולידציה ובניית הסטורג' כפי שעשית קודם
        if (validateSupply()) {
            buildStorages();
        }
    }

    public void loadSupplyXMLCheckAndBuildStorages(String path) throws Exception {
        loadSupplyFromXML(path);
        this.ValidSupply = validateSupply();
        buildStorages();
    }

     private boolean IsInSupplyRotorID(int id) {
         return RS.containsRotor(id);
     }

     public int getRotorsCount() {
         return rotorsCount;
     }

     public void setRotorsCount(int rotorsCount) {
         this.rotorsCount = rotorsCount;
     }

     private boolean IsInSupplyReflectorID(String id) {
         return RFS.containsReflector(id);
     }

     public Reflector optionalGetReflectorByID(String id) {
            if (!IsInSupplyReflectorID(id)) {
                throw (new IllegalArgumentException("Reflector ID " + id + " not found in storage."));
            }
            return RFS.getReflectorByID(id);
        }

     public Rotor optionalGetRotorByID(int id) {
        if (!IsInSupplyRotorID(id)) {
            throw (new IllegalArgumentException("Rotor ID " + id + " not found in storage."));
        }
         return RS.getRotorByID(id);
     }

    public String getABC() {
        return ABC;
    }


    public void printStorages() {
        System.out.print(this.toString());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PartsManager{");
        sb.append("RotorStorage=");
        sb.append(RS == null ? "null" : RS.toString());
        sb.append(", ReflectorStorage=");
        sb.append(RFS == null ? "null" : RFS.toString());
        sb.append('}');
        return sb.toString();
    }

    public List<Integer> getIndexInABC(List<Character> lst) {
        return lst.stream()
                .map(ch -> {
                    int index = ABC.indexOf(ch);
                    if (index == -1) {
                        throw new IllegalArgumentException("Character '" + ch + "' not found in alphabet.");
                    }
                    return index;
                })
                .toList();
    }

    public int getRotorsAmount() {
        return RS == null ? 0 : RS.getPartCount();
    }

    public int getReflectorsAmount() {
        return RFS == null ? 0 : RFS.getPartCount();
    }

    public void setABC(String ABC) {
        this.ABC = ABC;
    }

    public void setOriginalPosition(List<Character> originalPosition) {
        this.originalPosition = originalPosition;
    }

    public List<Character> getOriginalPosition() {
        return originalPosition;
    }

    public void resetUsedIds() {
       PCV.reset();
    }

    public String reflectorStorageString() {
        return RFS.availableReflectorsStr();
    }
    public String rotorStorageString() {
        return RS.availableRotorsStr();
    }


}
