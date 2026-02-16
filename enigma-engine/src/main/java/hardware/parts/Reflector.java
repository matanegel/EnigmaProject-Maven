package hardware.parts;

import hardware.WiringCables.WiringReflactor;

import java.io.Serializable;
import java.util.Objects;

public class Reflector implements Serializable {
   private final String ID;
   private final WiringReflactor wiringReflactor;

    public Reflector(String ID, WiringReflactor wiring) {
        this.ID = ID;
        this.wiringReflactor = wiring;

    }

    public int reflect(int index){
        return wiringReflactor.wiringRef[index];
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Reflector reflector = (Reflector) o;
        return Objects.equals(ID, reflector.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ID);
    }

    @Override
    public String toString() {
        return "Reflector " + ID + ":\n" +
                "    Wiring:\n" + wiringReflactor;
    }

    public String getID() {
        return ID;
    }
}
