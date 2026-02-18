# Enigma Machine Simulator
This project is a Java-based simulation of the famous Enigma Machine used during World War II. It provides a console-based User Interface (UI) to configure the machine, manage rotors, reflectors, and plugboard, and encode/decode messages.

## Features
- **Load Configuration**: Load machine settings from an XML file.
- **Machine Details**: View current machine configuration, including rotors, reflectors, and usage statistics.
- **Manual Configuration**: Manually set up the machine's rotors, positions, and plugboard.
- **Automatic Configuration**: Randomly configure the machine for quick testing.
- **Encryption/Decryption**: Process strings through the Enigma machine.
- **Reset**: Reset the machine to its initial state (original rotor positions).
- **History**: View the history of configurations and processed strings.
- **Save/Load State**: Save the current machine state to a binary file and load it back later. (**BONUS**)

## Plugboard
The plugboard swaps pairs of letters before and after the signal passes through the rotors. Configuration format: `A|Z,D|E` (A↔Z, D↔E) or consecutive pairs `ADZXCV`.

## Working with N Rotors
The machine supports a configurable number of rotors defined in the XML file. Select N rotors from the available pool, set their positions, and the stepping mechanism handles rotation automatically.

## Usage
Upon running the application, you will be presented with a menu:

0.  **Exit**: Closes the application.
1.  **Load XML file**: Provide the full path to an XML file defining the machine specifications.
2.  **Machine configuration**: Displays the current status, including rotor usage and encryption statistics.
3.  **Manual Configuration**: Step-by-step setup of the machine (selecting rotors, positions, reflector, plugboard).
4.  **Automatic Configuration**: Let the system choose a random valid configuration.
5.  **Encode/Decode String**: Input a string to be processed by the machine.
6.  **Reset to the original code**: Reverts rotors to their initial positions from the last configuration.
7.  **Show history**: Displays a log of all configurations and operations performed.
8.  **Save Machine to file**: Saves the machine state to a `.dat` file.
9.  **Load Machine from file**: Restores a previously saved machine state.

## Example Workflow

### Quick Start (Auto Configuration)
1. Run the application
2. Select option `1` → Enter path to XML configuration file
3. Select option `4` → Auto-configure the machine
4. Select option `5` → Enter `HELLO` → Receive encrypted output (e.g., `RILXZ`)
5. Select option `6` → Reset machine to original positions
6. Select option `5` → Enter `RILXZ` → Receive `HELLO` back
7. Select option `0` → Exit

### Manual Configuration
1. Run the application
2. Select option `1` → Enter path to XML configuration file (e.g., `C:\enigma\config.xml`)
3. Select option `3` → Manual Configuration:
   - Choose rotors from available list (e.g., enter `1,3,2` for rotors 1, 3, 2)
   - Set initial positions for each rotor (e.g., `ADK`)
   - Select reflector from available list (e.g., enter `1`)
   - Configure plugboard pairs (e.g., enter `ABCD` for A↔B and C↔D, or leave empty)
4. Select option `2` → View current machine configuration
5. Select option `5` → Enter message to encrypt/decrypt
6. Select option `7` → View history of all operations
7. Select option `0` → Exit

## Project Structure
- **console**: Console-based user interface and Main entry point.
- **engine**: Core Enigma processing (Rotors, Reflector, Plugboard, Engine).
- **machine**: Machine entity and state management.
- **loader**: XML configuration loading via JAXB.
- **archive**: History tracking and parts storage.

## Architecture & Flow
The system is built on a modular architecture separating the user interface, machine state, and core processing logic.

### High-Level Flow
- **Initialization**: The `Main` class initializes `MachineManager`.
- **Configuration**: User loads an XML via `EnigmaJaxbLoader`. `StorageManager` validates and stores the configuration parts like Rotors and Reflectors.
- **Assembly**: `MachineManager` assembles a `Machine` instance. The `Machine` holds the state and an `Engine`.
- **Processing**: The `Engine` directs the signal through the `Plugboard`, `Rotors`, and `Reflector` to encrypt/decrypt messages.

### Key Classes
- **`Main`**: Entry point for the application.
- **`MachineManager`**: The controller that orchestrates user choices, manages the `Machine` instance, and handles the flow of operations.
- **`Machine`**: Represents the Enigma Machine entity. It holds the current state (rotor positions), usage history, and the `Engine`.
- **`Engine`**: The processing core. It receives input characters and routes them through the hardware components.
- **`Rotor` & `Reflector`**: Software representations of the physical hardware components that perform the substitution and wiring logic.
- **`Plugboard`**: Performs bidirectional character swapping before and after rotor processing.

## Bonus (Exercise 1)
- **Save/Load State**: Save the current machine state to a binary file and load it back later.
