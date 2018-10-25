package com.notrust.server.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "program")
public class Program {
    private static final long serialVersionUID = 1L;

    @Id

    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "program_sequence_generator")
    @SequenceGenerator(name = "program_sequence_generator", sequenceName = "program_sequence", allocationSize = 1)
    private Long id;

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
