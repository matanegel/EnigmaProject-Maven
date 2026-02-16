package hardware.engine;

import hardware.parts.Plugboard;
import hardware.parts.Reflector;

import java.io.Serializable;

public class Engine implements Serializable {

    private final Reflector reflector;
    private final rotorsManagers manager;
    private final Plugboard plugboard;
    private final String alphabet;
    private int numberOfEncryptions = 0;

    public Engine(int rotorsCount, Reflector reflector, rotorsManagers manager, Plugboard plugboard, String alphabet) {
        if (manager.getRotors().length != rotorsCount) {
            throw new IllegalArgumentException(
                    "Exactly " + rotorsCount + " rotors are required to initialize the engine.");
        }
        this.reflector = reflector;
        this.manager = manager;
        this.plugboard = plugboard;
        this.alphabet = alphabet;
    }

    public char processChar(char ch) {
        int index = alphabet.indexOf(ch);
        if (index == -1) {
            throw new IllegalArgumentException("Char '" + ch + "' not in alphabet");
        }
        manager.stepRotors();

        // Pass through plugboard (input)
        int signal = plugboard.encode(index);

        // Pass through rotors forward
        signal = manager.passForward(signal);

        // Reflect
        signal = reflector.reflect(signal);

        // Pass through rotors backward
        signal = manager.passBackward(signal);

        // Pass through plugboard again (output)
        signal = plugboard.encode(signal);

        return alphabet.charAt(signal);
    }

    public String processString(String str) {
        StringBuilder sb = new StringBuilder();
        for (char ch : str.toCharArray()) {
            sb.append(processChar(ch));
        }
        numberOfEncryptions++;
        return sb.toString();
    }

    public rotorsManagers getRotorsManagers() {
        return manager;
    }

    public void trackRotorsState() {
        manager.printRotorsState();
    }

    public int getNumberOfEncryptions() {
        return numberOfEncryptions;
    }

    public String getReflectorId() {
        return reflector.getID();
    }

    public Plugboard getPlugboard() {
        return plugboard;
    }

}
