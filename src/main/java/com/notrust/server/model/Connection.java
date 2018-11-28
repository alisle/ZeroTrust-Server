package com.notrust.server.model;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "connection")
public class Connection {
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;

    @NotNull
    @Column(name = "agent_id", nullable = false)
    private UUID agent;

    @NotNull
    @Column(name = "connection_started", nullable = false)
    private Instant start;

    @Column(name = "connection_ended", nullable = true)
    private Instant end;

    @Column(name="duration", nullable = true)
    private long duration;

    @NotNull
    @Column(name="protocol", nullable =  false)
    private Protocol protocol;

    @NotNull
    @Column(name="source_address", nullable = false)
    private String source;

    @NotNull
    @Column(name="destination_address", nullable = false)
    private String destination;

    @NotNull
    @Column(name="source_port", nullable = false)
    private int sourcePort;

    @NotNull
    @Column(name="destination_port", nullable = false)
    private int destinationPort;


    @NotNull
    @Column(name = "username", nullable = false)
    private String username;

    @NotNull
    @Column(name = "uid", nullable = false)
    private int userID;

    @NotNull
    @OneToOne
    @JoinColumn(unique = true, nullable = false)
    private Program program;
}
