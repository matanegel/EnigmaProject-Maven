import hardware.Utils;
import hardware.parts.Rotor;
import history.ConfigurationStats;
import jaxb.EnigmaJaxbLoader;
import machine.Machine;
import software.AutoConfig;
import software.MachineConfig;
import software.ManualConfig;
import storage.StorageManager;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class MachineManager {

    private final Scanner scanner;

    public MachineManager(Scanner scanner) {
        this.scanner = scanner;
    }

    private final EnigmaJaxbLoader Loader = new EnigmaJaxbLoader();
    private StorageManager SM = new StorageManager(Loader);
    private Machine enigmaMachine = new Machine();
    boolean isFileLoaded = false;

    public void order1_readXmlFile() throws Exception {
        try {
            System.out.println("Supply XML path:");
            String path = scanner.nextLine().trim(); // to check path
            SM.resetUsedIds();
            StorageManager tempSM = new StorageManager(Loader);
            tempSM.loadSupplyXMLCheckAndBuildStorages(path);
            SM = tempSM;

            isFileLoaded = true;
            enigmaMachine.setRotorsCount(SM.getRotorsAmount());
            enigmaMachine.setAlphabet(SM.getABC());
            System.out.println("File loaded successfully");
        } catch (IllegalArgumentException iae) {
            throw new Exception("Invalid XML file: " + iae.getMessage());
        } catch (Exception e) {
            throw new Exception("Failed to load XML file: " + e.getMessage());
        }
    }

    public void order2_showMachineDetails() {
        if (!isFileLoaded) {
            System.out.println("XML File Not Loaded, Storage Are Empty!");
        }

        String sb = "Amount of rotors: " +
                SM.getRotorsAmount() +
                "\n" +
                "Amount of reflectors: " +
                SM.getReflectorsAmount() +
                "\n" +
                "amount of string that encoded: " +
                (enigmaMachine.getEngine() != null ? enigmaMachine.getEngine().getNumberOfEncryptions() : 0) +
                "\n" +
                "Original positions: " +
                getCode(true) +
                "\n" +
                "Current code: " +
                getCode(false);
        System.out.println(sb);
    }

    public void order3_manualMachineConfig() {
        if (!isFileLoaded) {
            throw new UnsupportedOperationException("XML File Not Loaded Yet - Make Order 1 First");
        }
        MachineConfig machineConfiguration = new ManualConfig(SM);
        this.enigmaMachine.setEngine(machineConfiguration.configureAndGetEngine());
        ConfigurationStats state = new ConfigurationStats(getCode(true));
        enigmaMachine.getFullHistory().add(state);
    }

    public void order4_autoMachineConfig() {
        if (!isFileLoaded) {
            throw new UnsupportedOperationException("XML File Not Loaded Yet - Make Order 1 First");
        }
        MachineConfig machineConfiguration = new AutoConfig(SM);
        this.enigmaMachine.setEngine(machineConfiguration.configureAndGetEngine());
        System.out.println("Auto Configuration Completed!");
        ConfigurationStats state = new ConfigurationStats(getCode(true));
        enigmaMachine.getFullHistory().add(state);
    }

    public void order5_encodeOrDecode() {
        if (enigmaMachine.getEngine() == null) {
            throw new UnsupportedOperationException("Engine Not Configured Yet - Make Order 3/4 First");
        }
        System.out.print("Write the string you want to encode/decode:\n");
        String input = scanner.nextLine().trim().toUpperCase();
        // start measure time
        long start = System.nanoTime();

        String cipher = enigmaMachine.getEngine().processString(input);
        // end measure time
        long end = System.nanoTime();

        System.out.printf("The result is:\n%s\n", cipher);

        if (!enigmaMachine.getFullHistory().isEmpty()) {
            // pull the last configuration stats
            enigmaMachine.getFullHistory().getLast().addProcessedString(input, cipher, (end - start));
        }

    }

    public void order6_restartMachineConfig() {
        if (enigmaMachine.getEngine() == null) {
            throw new UnsupportedOperationException("Engine Not Configured Yet - Make Order 3/4 First");
        }
        Rotor[] rotors = enigmaMachine.getEngine().getRotorsManagers().getRotors();
        List<Character> originalPosition = SM.getOriginalPosition();
        for (int i = 0; i < rotors.length; i++) {
            rotors[i].setPosition(rotors[i].getWiringRotor().getIndexOfChInRightColumn(originalPosition.get(i)));
        }
    }

    public void order7_showHistory() {
        showHistory();
    }

    public void order8_saveMachine() {
        if (!isFileLoaded) {
            throw new UnsupportedOperationException("XML File Not Loaded Yet - Make Order 1 First");
        }

        if (enigmaMachine.getEngine() == null) {
            throw new UnsupportedOperationException("Engine Not Configured Yet - Make Order 3/4 First");
        }

        System.out.print("Enter file name for BINARY save:");
        String filePathName = scanner.nextLine().trim();
        String finalFileName = filePathName.endsWith(".dat") ? filePathName : filePathName + ".dat";
        Path filePath = Paths.get(finalFileName);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
            // enigmaMachine.setAlphabet(SM.getABC());
            enigmaMachine.setOriginalPosition(SM.getOriginalPosition());
            enigmaMachine.setCurrentPosition(enigmaMachine.getEngine().getRotorsManagers().getRotorsPosotion());
            oos.writeObject(enigmaMachine);

            System.out.println("Full machine state saved successfully (BINARY) to: " + filePath.toAbsolutePath());

        } catch (NotSerializableException nse) {
            System.err.println(
                    "Serialization Error: A class is missing 'implements Serializable'. Please ensure all required classes (Rotor, Engine, etc.) are marked as serializable.");
            System.err.println("Error details: " + nse.getMessage());
        } catch (IOException e) {
            System.err.println("Error saving file: " + e.getMessage());
        }
    }

    public Machine order9_LoadMachine() {
        System.out.print("Enter file path for BINARY load: ");
        System.out.print("Enter file path for BINARY load: ");
        String filePathName = scanner.nextLine().trim();
        String finalFileName = filePathName.endsWith(".dat") ? filePathName : filePathName + ".dat";
        Path filePath = Paths.get(finalFileName);

        // Using ObjectInputStream to load the object
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
            // Reading the object and casting it to a MainManager instance
            Machine newEnigmaMachine = (Machine) ois.readObject();
            SM.setOriginalPosition(newEnigmaMachine.getOriginalPosition());
            List<Integer> currentPositions = newEnigmaMachine.getCurrentPosition();
            newEnigmaMachine.getEngine().getRotorsManagers().setRotorsPosition(currentPositions);
            SM.setRotorsCount(newEnigmaMachine.getRotorsCount());
            SM.setABC(newEnigmaMachine.getAlphabet());

            System.out.println(" Full machine state loaded successfully (BINARY) from: " + filePath.toAbsolutePath());
            return newEnigmaMachine; // Returning the loaded object
        } catch (FileNotFoundException e) {
            System.err.println("Load Error: File not found at path: " + filePath.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error reading data from file: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Load Error: The class structure has changed since saving. " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error during loading: " + e.getMessage());
        }
        return null; // If loading failed
    }

    public String getCode(boolean original) {
        if (enigmaMachine.getEngine() == null) {
            return "Machine Not Configured Yet - No Code Available";
        }
        return '<' +
                buildRotorString() +
                '>' +
                '<' +
                buildPositionString(original) +
                '>' +
                '<' +
                buildReflectorString() +
                '>' +
                '<' +
                buildPlugboardString() +
                '>';

    }

    public String buildRotorString() {
        StringBuilder sb = new StringBuilder();
        Rotor[] rotors = enigmaMachine.getEngine().getRotorsManagers().getRotors();
        int index = rotors.length - 1;
        for (int i = index; i >= 0; i--) {
            sb.append(rotors[i].getID());
            sb.append(',');
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public String buildReflectorString() {
        return enigmaMachine.getEngine().getReflectorId();
    }

    public String buildPositionString(boolean original) {
        StringBuilder sb = new StringBuilder();
        Rotor[] rotors = enigmaMachine.getEngine().getRotorsManagers().getRotors();
        List<Character> originalPosition = SM.getOriginalPosition();
        int index = rotors.length - 1;
        for (int i = index; i >= 0; i--) {
            sb.append(original ? originalPosition.get(i) : SM.getABC().charAt(rotors[i].getPosition()));
            sb.append('(');
            int res = rotors[i].getNoche()
                    - (original ? Utils.charToIndex(originalPosition.get(i), SM.getABC()) : rotors[i].getPosition());
            sb.append(Utils.normalize(res, rotors[i].sizeABC));
            sb.append(')');
            sb.append(',');
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public String buildPlugboardString() {
        String config = enigmaMachine.getEngine().getPlugboard().getConfigString();
        return config.isEmpty() ? "" : config;
    }

    public void setEnigmaMachine(Machine newEnigma) {
        enigmaMachine = newEnigma;
    }

    public void showHistory() {
        if (enigmaMachine.getFullHistory().isEmpty()) {
            System.out.println("\nNo history found. The machine hasn't been configured yet.");
            return;
        }
        System.out.println("\nHistory:\n");
        for (ConfigurationStats stats : enigmaMachine.getFullHistory()) {
            System.out.println(stats);
            System.out.println("----------------------------------------");
        }
    }
}
