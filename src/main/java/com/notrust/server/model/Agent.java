package com.notrust.server.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
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

    @ToString.Exclude
    @OneToMany(mappedBy = "agent")
    private Set<Connection> connections;

    @ToString.Exclude
    @Formula("select count(*) from connection where connection.agent_id = id")
    private long connectionCount;

    @ToString.Exclude
    @Formula("select count(*) from connection where connection.agent_id = id and connection.connection_ended IS NULL")
    private long aliveConnectionCount;


    @ToString.Exclude
    @Formula("id")
    private UUID uuid;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.DETACH
    })
    @JoinTable(name = "agent_ip_address",
            joinColumns = @JoinColumn(name = "agent_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "ip_address", referencedColumnName = "address"))
    private Set<IPAddress> addresses = new HashSet<>();

}

