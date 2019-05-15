package com.zerotrust.rest.model;

import lombok.Data;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "v_connection_link")
public class ViewConnectionLink {
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;

    @NotNull
    @Column(name = "connection_hash")
    private long connectionHash;

    @NotNull
    @Column(name = "created_on")
    private Instant timestamp;

    @Column(name = "ended_on")
    private Instant ended;

    @Column(name = "duration")
    private long duration;

    @Column(name = "source_address_string")
    private String sourceString;

    @Column(name = "source_address")
    private int sourceAddress;

    @Column(name = "destination_address_string")
    private String destinationString;

    @Column(name = "destination_address")
    private int destinationAddress;

    @Column(name = "source_port")
    private int sourcePort;

    @Column(name = "destination_port")
    private int destinationPort;

    @ManyToOne
    @JoinColumn(name="source_agent_id", nullable = false)
    private ViewAgent sourceAgent;

    @ManyToOne
    @JoinColumn(name="destination_agent_id", nullable = false)
    private ViewAgent destinationAgent;

    @OneToOne
    @JoinColumn(name="source_connection_id", nullable = true)
    private Connection sourceConnection;

    @OneToOne
    @JoinColumn(name="destination_connection_id", nullable = true)
    private Connection destinationConnection;


    // This is horrible, got to find a better way of doing this.
    @Column(name = "source_agent_name")
    private String sourceAgentName;

    // Mate, this too is horrible.
    @Column(name = "destination_agent_name")
    private String destinationAgentName;

    @Column(name = "destination_network_name")
    private String destinationNetworkName;

    @Column(name = "source_network_name")
    private String sourceNetworkName;

    @ManyToOne
    @JoinColumn(name = "source_network_id")
    private Network sourceNetwork;

    @ManyToOne
    @JoinColumn(name = "destination_network_id")
    private Network destinationNetwork;

    @NotNull
    @Column(name = "alive")
    private boolean alive;

    @Column(name = "source_process_name")
    private String sourceProcessName;

    @Column(name = "destination_process_name")
    private String destinationProcessName;

    @Column(name = "source_username")
    private String sourceUserName;

    @Column(name = "destination_username")
    private String destinationUserName;
}
