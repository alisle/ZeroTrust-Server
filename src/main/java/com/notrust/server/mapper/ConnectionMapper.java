package com.notrust.server.mapper;

import com.notrust.server.model.Connection;
import com.notrust.server.model.dto.ConnectionOpenDTO;
import org.springframework.stereotype.Service;

@Service
public class ConnectionMapper {
    private final ProgramMapper programMapper;

    public ConnectionMapper(ProgramMapper programMapper) {
        this.programMapper = programMapper;
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
        connection.setProgram(programMapper.DTOtoProgram(dto.getProgram()));

        return connection;
    }
}
