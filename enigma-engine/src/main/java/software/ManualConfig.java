package software;

import hardware.engine.Engine;
import hardware.engine.rotorsManagers;
import hardware.parts.Plugboard;
import hardware.parts.Reflector;
import hardware.parts.Rotor;

import java.util.*;

public class ManualConfig extends MachineConfig {
    Scanner sc = new Scanner(System.in);

    public ManualConfig(StorageProvider SM) {
        super(SM);
    }

    private List<Rotor> askRotors() {
        System.out.println("Enter rotors IDs separated by commas left to right, "
                + storageManager.rotorStorageString() + ":");
        String input = sc.nextLine().trim();
        String[] parts = input.split("\\s*,\\s*");
        Set<Integer> usedRotors = new HashSet<>();
        List<Rotor> rotors = new ArrayList<>();
        for (String part : parts) {
            int rotorId = Integer.parseInt(part);
            if (usedRotors.contains(rotorId)) {
                throw new IllegalStateException("Cannot build enigma machine with duplicate rotors");
            }
            usedRotors.add(rotorId);
            rotors.add(storageManager.optionalGetRotorByID(rotorId));
        }
        return new ArrayList<>(rotors.reversed());
    }

    private Reflector askReflector() {
        System.out.println("Choose one reflector " + storageManager.reflectorStorageString() + ": ");
        String input = sc.nextLine().trim();
        return storageManager.optionalGetReflectorByID(input);
    }

    private List<Character> askPositions() {
        System.out.println("Enter initial positions left to right, based on alphabet " + storageManager.getABC() + ":");
        String input = sc.nextLine().trim().toUpperCase();
        return breakPositionString(input);
    }

    private List<Character> breakPositionString(String positions) {
        // Returns IntStream
        // Cast int to Character
        return new ArrayList<>(positions.chars() // Returns IntStream
                .mapToObj(c -> (char) c) // Cast int to Character
                .toList().reversed());
    }

    private Plugboard askPlugboard() {
        System.out.println(
                "Enter plugboard configuration (consecutive pairs like 'ADZXCV' for A-D, Z-X, C-V connections).");
        System.out.println("Press ENTER for no plugboard connections:");
        String input = sc.nextLine().trim().toUpperCase();

        if (input.isEmpty()) {
            return new Plugboard(storageManager.getABC());
        }

        try {
            String pipeFormat = Plugboard.parsePairFormat(input);
            return new Plugboard(storageManager.getABC(), pipeFormat);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid plugboard configuration: " + e.getMessage());
        }
    }

    @Override
    public Engine configureAndGetEngine() {
        List<Rotor> rotors = askRotors();
        List<Character> positions = askPositions();
        if (rotors.size() != positions.size()) {
            throw new IllegalArgumentException("Number of positions must match rotors amount");
        }

        storageManager.setOriginalPosition(positions);
        Reflector reflector = askReflector();
        Plugboard plugboard = askPlugboard();

        int rotorsCount = storageManager.getRotorsCount();
        rotorsManagers manager = new rotorsManagers(rotors.toArray(new Rotor[0]));
        List<Integer> indexOfPositions = manager.MappingInputCharPositionByRightColumnToIndex(positions);
        manager.setRotorsPosition(indexOfPositions);

        return new Engine(rotorsCount, reflector, manager, plugboard, storageManager.getABC());
    }
}
