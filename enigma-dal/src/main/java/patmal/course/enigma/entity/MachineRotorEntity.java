package patmal.course.enigma.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "machines_rotors")
public class MachineRotorEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "machine_id")
    private UUID machineId;

    @Column(name = "rotor_id")
    private Integer rotorId;

    @Column(name = "notch")
    private Integer notch;

    @Column(name = "wiring_right")
    private String wiringRight;

    @Column(name = "wiring_left")
    private String wiringLeft;

    public MachineRotorEntity() {}
}
