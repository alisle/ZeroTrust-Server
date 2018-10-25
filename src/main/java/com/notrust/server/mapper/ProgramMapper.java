package com.notrust.server.mapper;

import com.notrust.server.model.Program;
import com.notrust.server.model.dto.ProgramDTO;
import org.springframework.stereotype.Service;

@Service
public class ProgramMapper {
    public Program DTOtoProgram(ProgramDTO dto) {
        Program program = new Program();
        program.setInode(dto.getInode());
        program.setPid(dto.getPid());
        program.setProcessName(dto.getProccessName());
        program.setCommandLine(String.join(" ", dto.getCommandLine()));

        return program;
    }
}
