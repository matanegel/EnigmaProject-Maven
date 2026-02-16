package patmal.course.enigma.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "machines_reflectors")
public class MachineReflectorEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "machine_id")
    private UUID machineId;

    @Column(name = "reflector_id")
    private String reflectorId; // Maps to your reflector_id_enum

    @Column(name = "input_text")
    private String input;

    @Column(name = "output_text")
    private String output;

    public MachineReflectorEntity() {}
}
