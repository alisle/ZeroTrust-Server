package com.zerotrust.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "v_agent")
public class ViewAgent {
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "firstSeen", nullable = true)
    private Instant firstSeen;

    @Column(name = "alive", nullable = false)
    private Boolean alive;

    @Column(name = "known", nullable = false)
    private Boolean known;

    @Column(name = "last_seen", nullable = false)
    private Instant lastSeen;

    @ToString.Exclude
    @OneToMany(mappedBy = "agent", fetch = FetchType.LAZY)
    @JsonIgnore
    @EqualsAndHashCode.Exclude private Set<Connection> connections;


    @ToString.Exclude
    @Column(name = "connection_count")
    @EqualsAndHashCode.Exclude private long connectionCount;

    @ToString.Exclude
    @Column(name = "alive_connection_count")
    @EqualsAndHashCode.Exclude private long aliveConnectionCount;

    @ToString.Exclude
    @Column(name = "source_connection_count")
    @EqualsAndHashCode.Exclude private long sourceConnectionCount;

    @ToString.Exclude
    @Column(name = "alive_source_connection_count")
    @EqualsAndHashCode.Exclude private long aliveSourceConnectionCount;


    @ToString.Exclude
    @Column(name = "destination_connection_count")
    @EqualsAndHashCode.Exclude private long destinationConnectionCount;


    @ToString.Exclude
    @Column(name = "alive_destination_connection_count")
    @EqualsAndHashCode.Exclude private long aliveDestinationConnectionCount;

    @ToString.Exclude
    @Formula("id")
    @EqualsAndHashCode.Exclude private UUID uuid;

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.DETACH
    })
    @JoinTable(name = "agent_ip_address",
            joinColumns = @JoinColumn(name = "agent_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "ip_address", referencedColumnName = "address"))
    @EqualsAndHashCode.Exclude private Set<IPAddress> addresses = new HashSet<>();

    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE,
            CascadeType.DETACH
    })
    @JoinTable(name = "agent_network",
            joinColumns = @JoinColumn(name = "agent_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "network_id", referencedColumnName = "id"))
    @EqualsAndHashCode.Exclude private Set<Network> networks = new HashSet<>();
}

