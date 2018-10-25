package com.notrust.server.service.impl;

import com.notrust.server.mapper.ConnectionMapper;
import com.notrust.server.mapper.ProgramMapper;
import com.notrust.server.model.Connection;
import com.notrust.server.model.dto.ConnectionCloseDTO;
import com.notrust.server.model.dto.ConnectionOpenDTO;
import com.notrust.server.repository.ConnectionRepository;
import com.notrust.server.service.ConnectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ConnectionServiceImpl implements ConnectionService {
    private final ConnectionMapper connectionMapper;
    private final ProgramMapper programMapper;
    private final ConnectionRepository repository;

    public ConnectionServiceImpl(ConnectionMapper connectionMapper, ProgramMapper programMapper, ConnectionRepository repository) {
        this.connectionMapper = connectionMapper;
        this.programMapper = programMapper;
        this.repository = repository;
    }

    @Override
    public Optional<Connection> open(ConnectionOpenDTO dto) {
        Connection connection = connectionMapper.OpenDTOtoConnection(dto);
        connection = repository.save(connection);
        return Optional.ofNullable(connection);
    }

    @Override
    public Optional<Connection> close(ConnectionCloseDTO dto) {
        if(dto.getId() == null) {
            //  We don't know what started this so we'll ignore it.
            return Optional.empty();
        }


        Optional<Connection> option = repository.findById(dto.getId());
        if(!option.isPresent()) {
            // We donn't seem to have the connection in our DB.
            return Optional.empty();
        }

        Connection connection = option.get();
        connection.setEnd(dto.getTimestamp());
        connection.setDuration(connection.getEnd().toEpochMilli() - connection.getStart().toEpochMilli());
        connection = repository.save(connection);

        return Optional.ofNullable(connection);
    }
}
