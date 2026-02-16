package hardware.engine;

import hardware.parts.Rotor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class rotorsManagers implements Serializable {
    private final Rotor[] rotors;

    public rotorsManagers(Rotor[] rotors) {
        this.rotors = rotors;
    }

    public void stepRotors(){
        rotors[0].rotate();
        checkRotate();
    }

    public void setRotorsPosition(List<Integer> position){
        if (position.size() != rotors.length){
            throw new IllegalArgumentException("Position list size does not match number of rotors");
        }
        int size = rotors.length;
        for (int i = 0; i < size ; i++) {
            int input = position.get(i);
            rotors[i].setPosition(input);
        }
    }

    public List<Integer> getRotorsPosotion() {
        List<Integer> positions = new ArrayList<>();
        for (Rotor rotor : rotors) {
            positions.add(rotor.getPosition());
        }
        return positions;
    }


    //check if the rotors need to rotate the next rotor
    public void checkRotate(){
       int sizeABC = rotors[0].sizeABC;
        for (int i = 0; i < rotors.length - 1; i++) {
            if (rotors[i].getNoche() == rotors[i].getPosition()) {
                rotors[i + 1].rotate();
            } else {
                break;
            }
        }
    }
    //passing the signal through the rotors from right to left
    public int passForward(int index) {
        int signal = index;

        for (Rotor rotor : rotors) {
            signal = rotor.encodeForward(signal);
        }

        return signal ;
    }
    //passing the signal through the rotors from left to right
    public int passBackward(int index) {
        int signal = index;
        for (int i = rotors.length - 1; i >= 0; i--) {
            signal = rotors[i].encodeBackward(signal);
        }
        return signal;
    }

    public void printRotorsState(){
        for (Rotor rotor : rotors) {
            System.out.printf("Rotor ID: %d, Position: %d, Noche: %d\n", rotor.getID(), rotor.getPosition(), rotor.getNoche());
        }
    }

    public Rotor[] getRotors() {
        return rotors;
    }

    public List<Integer> MappingInputCharPositionByRightColumnToIndex(List<Character> Inputs) {
        List<Integer> positions = new ArrayList<>();
        int index = 0;
        for (Rotor rotor : rotors) {
            positions.add(rotor.getWiringRotor().getIndexOfChInRightColumn(Inputs.get(index)));
            index++;
        }
        return positions;
    }
}

