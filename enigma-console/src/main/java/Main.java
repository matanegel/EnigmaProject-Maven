import machine.Machine;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        MachineManager machineManager = new MachineManager(sc);

        while (true) {
            System.out.println("""
                    \nCHOOSE AN ORDER:
                    1. Load XML file
                    2. Machine configuration
                    3. Manual Configuration
                    4. Automatic Configuration
                    5. Encode/Decode String
                    6. Reset to the original code
                    7. Show history
                    8. Save Machine to file
                    9. Load Machine from file
                    0. Exit""");
            String choice = sc.nextLine().trim();
            try {
                switch (choice) {
                    case "1" -> machineManager.order1_readXmlFile();
                    case "2" -> machineManager.order2_showMachineDetails();
                    case "3" -> machineManager.order3_manualMachineConfig();
                    case "4" -> machineManager.order4_autoMachineConfig();
                    case "5" -> machineManager.order5_encodeOrDecode();
                    case "6" -> machineManager.order6_restartMachineConfig();
                    case "7" -> machineManager.order7_showHistory();
                    case "8" -> machineManager.order8_saveMachine();
                    case "9" -> {
                        Machine loadedMachine = machineManager.order9_LoadMachine();

                        if (loadedMachine != null) {
                            machineManager.setEnigmaMachine(loadedMachine);
                        }
                    }
                    case "0" -> {
                        System.out.println("Exiting Program.");
                        return;
                    }
                    default -> System.out.println("Invalid choice, please try again.");
                }

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    };
};
