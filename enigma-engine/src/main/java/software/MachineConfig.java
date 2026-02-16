package software;


import hardware.engine.Engine;


public abstract class MachineConfig {
    protected final StorageProvider storageManager;

    public MachineConfig(StorageProvider SM) {
        this.storageManager = SM;
    }

    public abstract Engine configureAndGetEngine();
}

