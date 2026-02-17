package patmal.course.enigma.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "machines")
public class MachineEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "rotors_count")
    private int rotorsCount;

    @Column(name = "abc")
    private String abc;

    // One-to-Many relationships for easier data retrieval later
    @OneToMany(mappedBy = "machineId", cascade = CascadeType.ALL)
    private List<MachineRotorEntity> rotors;

    @OneToMany(mappedBy = "machineId", cascade = CascadeType.ALL)
    private List<MachineReflectorEntity> reflectors;

    // Default constructor, Getters & Setters
    public MachineEntity() {}
}