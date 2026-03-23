package patmal.course.enigma.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
@Builder
@Table(name = "processing")
@AllArgsConstructor
public class ProcessingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "machine_id", nullable = false)
    private MachineEntity machineId;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "code")
    private String code;

    @Column(name = "input")
    private String input;

    @Column(name = "output")
    private String output;

    @Column(name = "time")
    private long time; // Representing bigint (ns)

    public ProcessingEntity() {}
}
