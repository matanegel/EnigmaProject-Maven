package storage.rotor;


import hardware.parts.Rotor;
import storage.PartsStorage;

import java.util.Map;
import java.util.stream.Collectors;

public class RotorStorage implements PartsStorage {
    private final Map<Integer, Rotor> rotorMap;

    public RotorStorage(Map<Integer, Rotor> routorMap) {
        this.rotorMap = routorMap;
    }

    public Rotor getRotorByID(int id) {
        return rotorMap.get(id);
    }

    public boolean containsRotor(int id) {
        return rotorMap.containsKey(id);
    }

    public int getAmountOfRotors(){
        return rotorMap.size();
    }

    public String availableRotorsStr() {
        return "Available Rotors:" + rotorMap.keySet().stream()
                .sorted()                        // Sorts the stream naturally
                .map(String::valueOf)            // Converts Integers to Strings
                .collect(Collectors.joining(",")); // Joins with comma, no trailing comma issues
    }
    @Override
    public int getPartCount() {
        return rotorMap.size();
    }

    @Override
    public String toString() {
        return "RotorStorage: \n" + rotorMap.values();
    }
}
