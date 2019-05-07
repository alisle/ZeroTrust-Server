package com.zerotrust.links.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "network")
public class Network {
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable =  true)
    private String description;

    @NotNull
    @Column(name = "address", nullable = false)
    private int address;

    @NotNull
    @Column(name = "address_string", nullable = false)
    private String addressString;

    @NotNull
    @Column(name = "mask", nullable = false)
    private int mask;

    @NotNull
    @Column(name = "mask_string", nullable = false)
    private String maskString;

    @Formula("(SELECT COUNT(*) FROM connection_link WHERE connection_link.source_network_id = id)")
    @EqualsAndHashCode.Exclude private long sourceConnectionCount;

    @Formula("(SELECT COUNT(*) FROM connection_link WHERE connection_link.destination_network_id = id)")
    @EqualsAndHashCode.Exclude private long destinationConnectionCount;

    @Formula("(SELECT COUNT(*) FROM connection_link WHERE connection_link.source_network_id = id and connection_link.alive = true)")
    @EqualsAndHashCode.Exclude private long activeSourceConnectionCount;

    @Formula("(SELECT COUNT(*) FROM connection_link WHERE connection_link.destination_network_id = id and connection_link.alive = true)")
    @EqualsAndHashCode.Exclude private long activeDestinationConnectionCount;




    @ToString.Exclude
    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.DETACH
    })
    @JoinTable(name = "agent_network",
            joinColumns = @JoinColumn(name = "network_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "agent_id", referencedColumnName = "id"))
    @EqualsAndHashCode.Exclude private Set<Agent> agents = new HashSet<>();
}
