package com.notrust.server.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "agent")
public class Agent {
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "registered_at", nullable = true)
    private Instant registeredAt;

    @Column(name = "first_seen", nullable = true)
    private Instant firstSeen;

    @Column(name = "alive", nullable = false)
    private Boolean alive;

    @Column(name = "known", nullable = false)
    private Boolean known;

    @Column(name = "last_seen", nullable = false)
    private Instant lastSeen;

}

