package storage.reflector;


import hardware.parts.Reflector;
import storage.PartsStorage;

import java.util.Map;
import java.util.stream.Collectors;

public class ReflectorStorage implements PartsStorage {
    private Map<String, Reflector> reflectorMap;

    public ReflectorStorage(Map<String, Reflector> reflectors) {
        this.reflectorMap = reflectors;
    }

    public Reflector getReflectorByID(String id) {
        id = convertNumberToRome(id);
        return reflectorMap.get(id);
    }

    public boolean containsReflector(String id) {
        id = convertNumberToRome(id);
        return reflectorMap.containsKey(id);
    }

    public String availableReflectorsStr() {
        return "Available Reflectors:" + reflectorMap.keySet().stream()
                .map(this::convertRomeToNumber)   // 1. Convert Roman to Integer
                .sorted()                         // 2. Sort the Integers (1, 2, 3...)
                .map(String::valueOf)
                .map(this::convertNumberToRome)             // 3. Convert Integer -> String (CRITICAL STEP)
                .collect(Collectors.joining(","));// 4. Join Strings
    }

    @Override
    public int getPartCount() {
        return reflectorMap.size();
    }

    public void setReflectorMap(Map<String, Reflector> reflectors) {
        this.reflectorMap = reflectors;
    }

    @Override
    public String toString() {
        return "ReflectorStorage: \n" + reflectorMap.values();
    }

    private String convertNumberToRome(String id) {
        return switch (id) {
            case "1" -> "I";
            case "2" -> "II";
            case "3" -> "III";
            case "4" -> "IV";
            case "5" -> "V";
            default -> throw new IllegalArgumentException("Invalid reflector ID: " + id);
        };
    }

    private Integer convertRomeToNumber(String id) {
        return switch (id) {
            case "I" -> 1;
            case "II" -> 2;
            case "III" -> 3;
            case "IV" -> 4;
            case "V" -> 5;
            default -> throw new IllegalArgumentException("Invalid reflector ID: " + id);
        };
    }

}
