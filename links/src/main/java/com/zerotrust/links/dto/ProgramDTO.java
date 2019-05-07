package com.zerotrust.links.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProgramDTO {

    @JsonProperty("inode")
    private int inode;

    @JsonProperty("pid")
    private int pid;

    @JsonProperty("process_name")
    private String proccessName;

    @JsonProperty("command_line")
    private String[] commandLine;

}
