package com.notrust.server.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "connection_link")
public class ConnectionLink {
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;

    @NotNull
    @Column(name = "connection_hash")
    private long connectionHash;

    @NotNull
    @Column(name = "created_on")
    private Instant timestamp;

    @NotNull
    @ManyToOne
    @JoinColumn(name="source_agent_id", nullable = false)
    private Agent sourceAgent;

    @NotNull
    @ManyToOne
    @JoinColumn(name="destination_agent_id", nullable = false)
    private Agent destinationAgent;

}
