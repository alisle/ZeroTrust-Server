package com.notrust.server.mapper;

import com.notrust.server.model.Connection;
import com.notrust.server.model.dto.ConnectionOpenDTO;
import com.notrust.server.model.dto.ProgramDTO;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class ConnectionMapper {

    public ConnectionMapper() {

    }

    public Connection OpenDTOtoConnection(ConnectionOpenDTO dto) {
        Connection connection = new Connection();
        connection.setId(dto.getId());
        connection.setAgent(dto.getAgent());
        connection.setStart(dto.getTimestamp());
        connection.setProtocol(dto.getProtocol());
        connection.setSource(dto.getSource());
        connection.setDestination(dto.getDestination());
        connection.setSourcePort(dto.getSourcePort());
        connection.setDestinationPort(dto.getDestinationPort());
        connection.setUsername(dto.getUsername());
        connection.setUserID(dto.getUid());


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
