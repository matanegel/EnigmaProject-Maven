package machine;


import hardware.engine.Engine;
import history.ConfigurationStats;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Machine implements Serializable {
    private String name;
    private String alphabet;
    private int rotorsCount;
    private MachineState state;
    private List<Character> originalPosition;
    private List<Integer> currentPosition;
    private Engine engine;
    private List<ConfigurationStats> fullHistory = new ArrayList<>();


    public MachineState getState() {
        return state;
    }
    public void setAlphabet(String alphabet) {
        this.alphabet = alphabet;
    }
    public void setRotorsCount(int rotorsCount) {
        this.rotorsCount = rotorsCount;
    }

    public int getRotorsCount() {
        return rotorsCount;
    }

    public String getAlphabet() {
        return alphabet;
    }

    public List<Integer> getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(List<Integer> currentPosition) {
        this.currentPosition = currentPosition;
    }

    public List<Character> getOriginalPosition() {
        return originalPosition;
    }

    public void setOriginalPosition(List<Character> originalPosition) {
        this.originalPosition = originalPosition;
    }

    public void setState(MachineState state) {
        this.state = state;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public List<ConfigurationStats> getFullHistory() {
        return fullHistory;
    }

    public void setFullHistory(List<ConfigurationStats> fullHistory) {
        this.fullHistory = fullHistory;
    }
}
