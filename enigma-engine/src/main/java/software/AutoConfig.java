package software;

import hardware.engine.Engine;
import hardware.engine.rotorsManagers;
import hardware.parts.Plugboard;
import hardware.parts.Reflector;
import hardware.parts.Rotor;

import java.util.ArrayList;
import java.util.List;

public class AutoConfig extends MachineConfig {

    public AutoConfig(StorageProvider SM) {
        super(SM);
    }

    private int getRandomIntInRange(int min, int max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    private List<Character> generateRandomPositions(int size) {
        List<Character> positions = new ArrayList<>();
        String abc = storageManager.getABC();
        int abcLength = abc.length();

        for (int i = 0; i < size; i++) {
            int randomIndex = getRandomIntInRange(0, abcLength - 1);
            positions.add(abc.charAt(randomIndex));
        }
        return positions;
    }

    private List<Rotor> getRandomRotors(int count) {
        List<Rotor> selectedRotors = new ArrayList<>();
        List<Integer> usedIndices = new ArrayList<>();

        while (selectedRotors.size() < count) {
            int randomIndex = getRandomIntInRange(1, storageManager.getRotorsAmount());
            if (!usedIndices.contains(randomIndex)) {
                usedIndices.add(randomIndex);
                selectedRotors.add(storageManager.optionalGetRotorByID(randomIndex));
            }
        }
        return selectedRotors;
    }

    private Reflector getRandomReflector() {
        int reflectorId = getRandomIntInRange(1, storageManager.getReflectorsAmount());
        return storageManager.optionalGetReflectorByID(String.valueOf(reflectorId));
    }

    private Plugboard getRandomPlugboard() {
        String abc = storageManager.getABC();
        List<Character> availableChars = new ArrayList<>();

        // Add all characters to available pool
        for (char c : abc.toCharArray()) {
            availableChars.add(c);
        }

        // Randomly decide how many pairs to create (0 to half the alphabet size)
        int maxPairs = abc.length() / 2;
        int numPairs = getRandomIntInRange(0, maxPairs);

        StringBuilder pairString = new StringBuilder();

        for (int i = 0; i < numPairs; i++) {
            if (availableChars.size() < 2) {
                break; // Not enough characters left
            }

            // Pick two random characters
            int index1 = getRandomIntInRange(0, availableChars.size() - 1);
            char char1 = availableChars.remove(index1);

            int index2 = getRandomIntInRange(0, availableChars.size() - 1);
            char char2 = availableChars.remove(index2);

            pairString.append(char1).append(char2);
        }

        // If no pairs generated, return identity mapping
        if (pairString.length() == 0) {
            return new Plugboard(abc);
        }

        // Convert pair format to pipe format and create plugboard
        String pipeFormat = Plugboard.parsePairFormat(pairString.toString());
        return new Plugboard(abc, pipeFormat);
    }

    @Override
    public Engine configureAndGetEngine() {
        int rotorsCount = storageManager.getRotorsCount();
        List<Rotor> rotors = getRandomRotors(rotorsCount);
        List<Character> positions = generateRandomPositions(rotorsCount);
        storageManager.setOriginalPosition(positions);
        Reflector reflector = getRandomReflector();

        rotorsManagers manager = new rotorsManagers(rotors.toArray(new Rotor[0]));
        List<Integer> indexOfPositions = manager.MappingInputCharPositionByRightColumnToIndex(positions);
        manager.setRotorsPosition(indexOfPositions);

        // Create random plugboard configuration
        Plugboard plugboard = getRandomPlugboard();

        return new Engine(rotorsCount, reflector, manager, plugboard, storageManager.getABC());
    }
}
