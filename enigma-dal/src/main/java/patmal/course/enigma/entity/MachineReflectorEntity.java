package patmal.course.enigma.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import storage.reflector.reflector_id_enum;


import java.util.UUID;

@Entity
@Data
@Table(name = "machines_reflectors")
public class MachineReflectorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "machine_id", nullable = false)
    private MachineEntity machineId;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "reflector_id")
    private reflector_id_enum reflectorId;

    @Column(name = "input")
    private String input;

    @Column(name = "output")
    private String output;

    public MachineReflectorEntity() {}
}
