package software;

import hardware.parts.Reflector;
import hardware.parts.Rotor;

import java.util.List;

/**
 * Abstraction over the parts storage used by the engine configuration layer.
 * Implemented by the actual storage (e.g., archive.StorageManager) to avoid
 * a circular dependency between engine and archive modules.
 */
public interface StorageProvider {
    String getABC();

    int getRotorsAmount();

    int getReflectorsAmount();

    Rotor optionalGetRotorByID(int id);

    Reflector optionalGetReflectorByID(String id);

    void setOriginalPosition(List<Character> originalPosition);

    String rotorStorageString();

    String reflectorStorageString();

    int getRotorsCount();
}
