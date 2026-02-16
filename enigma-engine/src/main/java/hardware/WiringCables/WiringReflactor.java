package hardware.WiringCables;


import java.io.Serializable;
import java.util.Arrays;

public class WiringReflactor implements Serializable {
    public final int [] wiringRef;

    public WiringReflactor(String input , String output, int alphabetSize) {
        this.wiringRef = new int[alphabetSize];
        Arrays.fill(this.wiringRef, -1);

        String[] inTokens = input.trim().split("[,\\s]+");
        String[] outTokens = output.trim().split("[,\\s]+");

        if (inTokens.length != outTokens.length) {
            throw new IllegalArgumentException("Input and output must have same length");
        }

        for (int i = 0; i < inTokens.length; i++) {
            int in = Integer.parseInt(inTokens[i]) - 1;
            int out = Integer.parseInt(outTokens[i]) - 1;

            if (in < 0 || in >= alphabetSize || out < 0 || out >= alphabetSize) {
                throw new IllegalArgumentException("Index out of range for reflector alphabet");
            }

            if (in == out) {
                throw new IllegalArgumentException("Reflector cannot map an index to itself");
            }
            if (this.wiringRef[in] != -1 || wiringRef[out] != -1) {
                throw new IllegalArgumentException("Index appears more than once in reflector mapping");
            }
            this.wiringRef[in] = out;
            this.wiringRef[out] = in;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < wiringRef.length; i++) {
            sb.append(String.format("         %d: (%d -> %d)",i, i + 1 , wiringRef[i] + 1));
            if (i % 5 == 0) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}
