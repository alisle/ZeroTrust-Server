package com.notrust.server.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "agent")
@EqualsAndHashCode(exclude="connections")
public class Agent {
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "registered_at", nullable = true)
    private Instant registeredAt;

    @Column(name = "firstSeen", nullable = true)
    private Instant firstSeen;

    @Column(name = "alive", nullable = false)
    private Boolean alive;

    @Column(name = "known", nullable = false)
    private Boolean known;

    @Column(name = "last_seen", nullable = false)
    private Instant lastSeen;

    @OneToMany(mappedBy = "agent")
    private Set<Connection> connections;
}

