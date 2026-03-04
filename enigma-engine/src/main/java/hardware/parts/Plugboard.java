package hardware.parts;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Plugboard component for the Enigma machine.
 * Maps characters bidirectionally based on configuration.
 * Default behavior is identity mapping (each character maps to itself).
 */
public class Plugboard implements Serializable {
    private final String alphabet;
    private final int[] mapping; // Array for O(1) bidirectional mapping
    private final int alphabetSize;

    /**
     * Creates a Plugboard with identity mapping (no swaps).
     * 
     * @param alphabet The alphabet string used by the machine
     */
    public Plugboard(String alphabet) {
        this(alphabet, null);
    }

    /**
     * Creates a Plugboard with specified configuration.
     * 
     * @param alphabet The alphabet string used by the machine
     * @param config   Configuration string in format "A|Z,D|E" (can be null for
     *                 identity mapping)
     */
    public Plugboard(String alphabet, String config) {
        if (alphabet == null || alphabet.isEmpty()) {
            throw new IllegalArgumentException("Alphabet cannot be null or empty");
        }

        this.alphabet = alphabet;
        this.alphabetSize = alphabet.length();
        this.mapping = new int[alphabetSize];

        // Initialize with identity mapping
        for (int i = 0; i < alphabetSize; i++) {
            mapping[i] = i;
        }

        // Apply configuration if provided
        if (config != null && !config.trim().isEmpty()) {
            configure(config);
        }
    }

    /**
     * Parses pair format string (e.g., "ADZXCV") and converts to pipe format (e.g.,
     * "A|D,Z|X,C|V").
     * Input string must have even length.
     * 
     * @param pairString String with consecutive character pairs
     * @return Pipe-delimited format string
     */
    public static String parsePairFormat(String pairString) {
        if (pairString == null || pairString.isEmpty()) {
            return "";
        }

        String trimmed = pairString.trim();
        if (trimmed.length() % 2 != 0) {
            throw new IllegalArgumentException(
                    "Plugboard pair string must have even length. Got: '" + trimmed + "' (length: " + trimmed.length()
                            + ")");
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < trimmed.length(); i += 2) {
            if (i > 0) {
                sb.append(",");
            }
            sb.append(trimmed.charAt(i)).append("|").append(trimmed.charAt(i + 1));
        }
        return sb.toString();
    }

    /**
     * Configures the plugboard with character pairs.
     * Format: "A|Z,D|E" where A connects to Z and D connects to E.
     * 
     * @param config Configuration string
     */
    private void configure(String config) {
        String trimmed = config.trim();
        if (trimmed.isEmpty()) {
            return;
        }

        Set<Integer> usedIndices = new HashSet<>();
        String[] pairs = trimmed.split(",");

        for (String pair : pairs) {
            pair = pair.trim();
            if (pair.isEmpty()) {
                continue;
            }

            String[] chars = pair.split("\\|");
            if (chars.length != 2) {
                throw new IllegalArgumentException(
                        "Invalid plugboard pair format: '" + pair + "'. Expected format: 'A|Z'");
            }

            String char1Str = chars[0].trim();
            String char2Str = chars[1].trim();

            if (char1Str.length() != 1 || char2Str.length() != 1) {
                throw new IllegalArgumentException(
                        "Each side of plugboard pair must be a single character: '" + pair + "'");
            }

            char char1 = char1Str.charAt(0);
            char char2 = char2Str.charAt(0);

            int index1 = alphabet.indexOf(char1);
            int index2 = alphabet.indexOf(char2);

            if (index1 == -1) {
                throw new IllegalArgumentException("Character '" + char1 + "' not found in alphabet");
            }
            if (index2 == -1) {
                throw new IllegalArgumentException("Character '" + char2 + "' not found in alphabet");
            }

            if (char1 == char2) {
                throw new IllegalArgumentException("Cannot plug a character to itself: '" + char1 + "'");
            }

            // Check if either character is already plugged
            if (usedIndices.contains(index1)) {
                throw new IllegalArgumentException("Character '" + char1 + "' is already plugged to another character");
            }
            if (usedIndices.contains(index2)) {
                throw new IllegalArgumentException("Character '" + char2 + "' is already plugged to another character");
            }

            // Create bidirectional mapping
            mapping[index1] = index2;
            mapping[index2] = index1;

            usedIndices.add(index1);
            usedIndices.add(index2);
        }
    }

    /**
     * Encodes a signal through the plugboard.
     * This operation is bidirectional (symmetric).
     * 
     * @param index The input signal index (0 to alphabetSize-1)
     * @return The output signal index after plugboard transformation
     */
    public int encode(int index) {
        if (index < 0 || index >= alphabetSize) {
            throw new IllegalArgumentException("Index out of bounds: " + index);
        }
        return mapping[index];
    }

    /**
     * Gets the character that the given character is plugged to.
     * 
     * @param ch The input character
     * @return The character it's plugged to (or itself if not plugged)
     */
    public char getPluggedChar(char ch) {
        int index = alphabet.indexOf(ch);
        if (index == -1) {
            throw new IllegalArgumentException("Character '" + ch + "' not in alphabet");
        }
        return alphabet.charAt(mapping[index]);
    }

    /**
     * Checks if a character is plugged (i.e., not mapped to itself).
     * 
     * @param ch The character to check
     * @return true if the character is plugged to another character
     */
    public boolean isPlugged(char ch) {
        int index = alphabet.indexOf(ch);
        if (index == -1) {
            throw new IllegalArgumentException("Character '" + ch + "' not in alphabet");
        }
        return mapping[index] != index;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Plugboard:\n");
        Set<Integer> printed = new HashSet<>();

        for (int i = 0; i < alphabetSize; i++) {
            if (mapping[i] != i && !printed.contains(i)) {
                char char1 = alphabet.charAt(i);
                char char2 = alphabet.charAt(mapping[i]);
                sb.append("    ").append(char1).append(" ↔ ").append(char2).append("\n");
                printed.add(i);
                printed.add(mapping[i]);
            }
        }

        if (printed.isEmpty()) {
            sb.append("    (No connections - identity mapping)\n");
        }

        return sb.toString();
    }

    /**
     * Gets the plugboard configuration as a pipe-delimited string (e.g.,
     * "A|Z,D|E").
     * Returns empty string if no connections are configured.
     * 
     * @return Configuration string in pipe format
     */
    public String getConfigString() {
        StringBuilder sb = new StringBuilder();
        Set<Integer> processed = new HashSet<>();

        for (int i = 0; i < alphabetSize; i++) {
            if (mapping[i] != i && !processed.contains(i)) {
                if (sb.length() > 0) {
                    sb.append(",");
                }
                char char1 = alphabet.charAt(i);
                char char2 = alphabet.charAt(mapping[i]);
                sb.append(char1).append("|").append(char2);
                processed.add(i);
                processed.add(mapping[i]);
            }
        }

        return sb.toString();
    }
}
