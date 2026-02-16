package patmal.course.enigma.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "processing")
public class ProcessingEntity {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "machine_id")
    private UUID machineId;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "code")
    private String code;

    @Column(name = "input_text")
    private String input;

    @Column(name = "output_text")
    private String output;

    @Column(name = "time")
    private long time; // Representing bigint (ns)

    public ProcessingEntity() {}
}
