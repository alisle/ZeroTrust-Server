package com.notrust.server.mapper;

import com.notrust.server.exception.AgentNotFoundException;
import com.notrust.server.model.Agent;
import com.notrust.server.model.Connection;
import com.notrust.server.model.dto.ConnectionOpenDTO;
import com.notrust.server.model.dto.ProgramDTO;
import com.notrust.server.service.AgentService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class ConnectionMapper {

    private final AgentService agentService;

    public ConnectionMapper(AgentService agentService) {
        this.agentService = agentService;
    }

    public Connection OpenDTOtoConnection(ConnectionOpenDTO dto) {
        Connection connection = new Connection();
        connection.setId(dto.getId());

        Agent agent = agentService.get(dto.getAgent())
                .orElseGet(() -> {
                    return agentService.unknown(dto.getAgent());
                });

        connection.setAgent(agent);
        connection.setStart(dto.getTimestamp());
        connection.setProtocol(dto.getProtocol());
        connection.setSource(dto.getSource());
        connection.setDestination(dto.getDestination());
        connection.setSourcePort(dto.getSourcePort());
        connection.setDestinationPort(dto.getDestinationPort());
        connection.setUsername(dto.getUsername());
        connection.setUserID(dto.getUid());
        connection.setConnectionHash(dto.getHash());


        ProgramDTO programDTO = dto.getProgram();
        if(programDTO != null) {
            connection.setCommandLine(Arrays.stream(programDTO.getCommandLine()).collect(Collectors.joining(" ")));
            connection.setInode(programDTO.getInode());
            connection.setPid(programDTO.getPid());
            connection.setProcessName(programDTO.getProccessName());
        }

        return connection;
    }

}
