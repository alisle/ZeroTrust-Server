package com.zerotrust.server.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
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

}
