package com.zerotrust.rest.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "connection")
@EqualsAndHashCode(exclude="agent")
public class Connection {
    private static final long serialVersionUID = 1L;

    @Id
    private UUID id;

    @NotNull
    @Column(name="connection_hash", nullable = false)
    private long connectionHash;

    @NotNull
    @ManyToOne
    @JoinColumn(name="agent_id", nullable = false)
    private ViewAgent agent;

    @NotNull
    @Column(name = "connection_started", nullable = false)
    private Instant start;

    @Column(name = "connection_ended", nullable = true)
    private Instant end;

    @Formula("connection_ended IS NULL")
    private boolean alive;

    @Column(name="duration", nullable = true)
    private long duration;

    @NotNull
    @Column(name="protocol", nullable =  false)
    private Protocol protocol;

    @ManyToOne
    @JoinColumn(name="source_network_id", nullable = true)
    private Network sourceNetwork;

    @NotNull
    @Column(name="source_address", nullable = false)
    private int source;

    @NotNull
    @Column(name="source_address_string", nullable = false)
    private String sourceString;

    @ManyToOne
    @JoinColumn(name="destination_network_id", nullable = true)
    private Network destinationNetwork;

    @NotNull
    @Column(name="destination_address", nullable = false)
    private int destination;

    @NotNull
    @Column(name="destination_address_string", nullable = false)
    private String destinationString;

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
    @Column(name = "inode", nullable = false)
    private int inode;

    @NotNull
    @Column(name = "pid", nullable = false)
    private int pid;

    @NotNull
    @Column(name = "process_name", nullable = false)
    private String processName;

    @NotNull
    @Column(name = "command_line", nullable = false)
    private String commandLine;

}
