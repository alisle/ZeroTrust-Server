package com.zerotrust.links;

import com.zerotrust.links.dto.ConnectionCloseDTO;
import com.zerotrust.links.dto.ConnectionOpenDTO;
import com.zerotrust.links.dto.ProgramDTO;
import com.zerotrust.model.Protocol;

import java.time.Instant;
import java.util.UUID;

public class CreationUtils {
    public static ProgramDTO ProgramDTO() {
        ProgramDTO dto = new ProgramDTO();
        dto.setInode(100);
        dto.setPid(101);
        dto.setProccessName("process name");
        dto.setCommandLine(new String[] { "I", "like", "strange", "things"});


        return dto;
    }

    public static ConnectionCloseDTO ConnectionCloseDTO() {
        ConnectionCloseDTO dto = new ConnectionCloseDTO();
        dto.setId(UUID.randomUUID());
        dto.setAgent(UUID.randomUUID());
        dto.setHash(1000L);
        dto.setTimestamp(Instant.now());
        dto.setProtocol(Protocol.TCP);
        dto.setSource("127.0.0.1");
        dto.setDestination("10.10.10.10");
        dto.setSourcePort(22);
        dto.setDestinationPort(22);

        return dto;
    }

    public static ConnectionOpenDTO ConnectionNewDTO() {
        ConnectionOpenDTO dto = new ConnectionOpenDTO();
        dto.setId(UUID.randomUUID());
        dto.setAgent(UUID.randomUUID());
        dto.setHash(10000L);
        dto.setTimestamp(Instant.now());
        dto.setProtocol(Protocol.TCP);
        dto.setSource("127.0.0.1");
        dto.setSourcePort(5123);
        dto.setDestination("10.10.10.10");
        dto.setDestinationPort(1010);
        dto.setUsername("root");
        dto.setUid(0);
        dto.setProgram(ProgramDTO());

        return dto;
    }

}
