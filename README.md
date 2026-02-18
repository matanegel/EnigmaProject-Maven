# Enigma Machine Simulator & Cryptanalysis Engine

## Overview
An advanced Java-based simulation of the WWII Enigma Machine, featuring a full cryptographic engine, a modern **JavaFX GUI**, and a high-performance **Brute Force Decryption** system (Cryptanalysis). 

The system is architected as a full-stack application, utilizing **Spring Boot** for back-end logic and REST APIs to manage machine states and processing.

## Key Features
- [cite_start]**Full Machine Simulation**: Custom configuration of N-rotors, reflectors, and plugboards via XML.
- **Spring Boot Back-end**: Robust RESTful API to manage engine state, encryption, and decryption requests.
- [cite_start]**JavaFX Modern GUI**: A responsive user interface featuring real-time status updates and visual rotor tracking.
- **Cryptanalysis Engine (The "Cracker")**: 
  - **Brute Force Attack**: High-performance decryption targeting the machine's initial configuration.
  - [cite_start]**Multithreading & Concurrency**: Utilizes a **Thread Pool Executor** to distribute decryption tasks across CPU cores for maximum efficiency.
- [cite_start]**Dictionary-Based Filtering**: Integrated dictionary validation to filter meaningful results from decrypted candidates[cite: 1].

## Architecture
The project follows a modular, scalable design using modern **Design Patterns**:
- **RESTful API**: Spring Boot server handling core logic and client requests.
- [cite_start]**DTOs (Data Transfer Objects)**: For immutable and thread-safe data exchange between the engine and UI[cite: 1].
- [cite_start]**Design Patterns**: Implemented **Singleton** for resource management and **Observer** for real-time progress tracking[cite: 1].
- [cite_start]**Concurrency**: Leveraged the Java Concurrency API for high-speed parallel processing[cite: 1].

## Tech Stack
- [cite_start]**Language**: Java 11+.
- **Back-end**: **Spring Boot** (REST, Dependency Injection).
- [cite_start]**Front-end**: JavaFX[cite: 1].
- [cite_start]**Build Tool**: Maven[cite: 1].
- [cite_start]**Configuration**: JAXB (XML Parsing)[cite: 1].

## Project Structure
- [cite_start]`engine`: Core cryptographic and mathematical logic[cite: 1].
- `web-server`: Spring Boot application and REST controllers.
- [cite_start]`gui-client`: JavaFX implementation and client-side logic[cite: 1].
- [cite_start]`dto`: Shared data structures for system-wide communication[cite: 1].

## How to Run
1. **Back-end**: Run the Spring Boot application (using Maven: `mvn spring-boot:run`).
2. **Client**: Launch the JavaFX application from the `gui` package.
3. [cite_start]**Setup**: Load a valid machine definition XML file and start encrypting[cite: 1].

## Contributors
* [cite_start]**Matan Egel** & **Edan Globus** - Core logic, Spring Boot integration, Multithreading & Cryptanalysis.
