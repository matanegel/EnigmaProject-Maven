package hardware.WiringCables;

import java.io.Serializable;

public class WiringRotor implements Wiring, Serializable {
    private String alphabet;
    private String rightColumn;
    private String leftColumn;

    public int [] wiringForwards;
    public int [] wiringBackwards;


    public WiringRotor(String rightColumn, String leftColumn, String alphabet) {
        this.alphabet = alphabet;
        this.rightColumn = rightColumn;
        this.leftColumn = leftColumn;
        this.wiringForwards = new int[alphabet.length()];
        this.wiringBackwards = new int[alphabet.length()];

        // Wires in the columns must match the alphabet size
        if (rightColumn.length() != alphabet.length() || leftColumn.length() != alphabet.length()) {
            throw new IllegalArgumentException("Columns must match alphabet size");
        }

        for (int i = 0; i < alphabet.length(); i++) {
            char letter = alphabet.charAt(i);
            // Check for duplicate letters in the alphabet
            if (rightColumn.indexOf(letter) != rightColumn.lastIndexOf(letter) ||
                    leftColumn.indexOf(letter) != leftColumn.lastIndexOf(letter)) {
                throw new IllegalArgumentException(
                        "Letter '" + letter + "' appears multiple times in a column");
            }


            int rightIndex = rightColumn.indexOf(letter); //find the letter in the right column
            int leftIndex = leftColumn.indexOf(letter);  // find the letter in the left column

            // Ensure the letter exists in both columns
            if (rightIndex == -1 || leftIndex == -1) {
                throw new IllegalArgumentException(
                        "Letter '" + letter + "' must appear in both columns");
            }

            //connect the wire
            this.wiringForwards[rightIndex] = leftIndex;
            this.wiringBackwards[leftIndex] = rightIndex;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < wiringForwards.length; i++) {
            sb.append(String.format("           %d: (%d <-> %d),",i, i + 1, wiringForwards[i] + 1));
            if (i % 5 == 0) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    public int getIndexOfChInRightColumn(char ch){
        return rightColumn.indexOf(ch);
    }
}
