package patmal.course.enigma.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Table(name = "processing")
public class ProcessingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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
