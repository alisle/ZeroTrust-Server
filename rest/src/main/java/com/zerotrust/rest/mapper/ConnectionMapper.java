package com.zerotrust.rest.mapper;

import com.zerotrust.rest.exception.InvalidIPAddress;
import com.zerotrust.rest.model.Agent;
import com.zerotrust.rest.model.Connection;
import com.zerotrust.rest.model.IPAddress;
import com.zerotrust.rest.model.dto.ConnectionOpenDTO;
import com.zerotrust.rest.model.dto.ProgramDTO;
import com.zerotrust.rest.service.AgentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ConnectionMapper {

    private final AgentService agentService;
    private final IPMapper ipMapper;

    public ConnectionMapper(AgentService agentService, IPMapper ipMapper) {
        this.agentService = agentService;
        this.ipMapper = ipMapper;
    }

    public Connection OpenDTOtoConnection(ConnectionOpenDTO dto) throws InvalidIPAddress {
        Connection connection = new Connection();
        connection.setId(dto.getId());

        Agent agent = agentService.get(dto.getAgent())
                .orElseGet(() -> {
                    return agentService.unknown(dto.getAgent());
                });

        connection.setAgent(agent);
        connection.setStart(dto.getTimestamp());
        connection.setProtocol(dto.getProtocol());
        connection.setSourceString(dto.getSource());
        connection.setDestinationString(dto.getDestination());
        connection.setSourcePort(dto.getSourcePort());
        connection.setDestinationPort(dto.getDestinationPort());
        connection.setUsername(dto.getUsername());
        connection.setUserID(dto.getUid());
        connection.setConnectionHash(dto.getHash());

        IPAddress sourceAddress = ipMapper.convertString(dto.getSource());
        IPAddress destinationAddress = ipMapper.convertString(dto.getDestination());

        connection.setSource(sourceAddress.getAddress());
        connection.setDestination(destinationAddress.getAddress());

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
