package com.zerotrust.oauth.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Entity
@Table(name="config")
public class Config {
    @Id
    @NotNull
    @Column(name="node")
    private UUID node;

    @NotNull
    @Column(name="first_run")
    private boolean firstRun;
}
