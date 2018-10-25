package com.notrust.server.mapper;

import com.notrust.server.CreationUtils;
import com.notrust.server.ServerApplication;
import com.notrust.server.model.Program;
import com.notrust.server.model.dto.ProgramDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ServerApplication.class)
public class ProgramMapperTest {

    @Autowired
    private ProgramMapper mapper;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDTOProgram() {
        ProgramDTO dto = CreationUtils.ProgramDTO();

        Program program = mapper.DTOtoProgram(dto);
        Assert.assertEquals(dto.getInode(), program.getInode());
        Assert.assertEquals(dto.getPid(), program.getPid());
        Assert.assertEquals(dto.getProccessName(), program.getProcessName());
        Assert.assertEquals(String.join(" ", dto.getCommandLine()), program.getCommandLine());

    }
}
