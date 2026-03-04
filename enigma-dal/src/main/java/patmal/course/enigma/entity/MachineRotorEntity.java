package patmal.course.enigma.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "machines_rotors")
public class MachineRotorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "machine_id", nullable = false)
    private MachineEntity machineId;

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
