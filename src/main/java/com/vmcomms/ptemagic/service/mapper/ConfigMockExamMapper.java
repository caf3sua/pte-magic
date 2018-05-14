package com.vmcomms.ptemagic.service.mapper;

import org.mapstruct.Mapper;

import com.vmcomms.ptemagic.domain.ConfigMockExam;
import com.vmcomms.ptemagic.dto.ConfigMockExamDTO;

@Mapper(componentModel = "spring", uses = {})
public interface ConfigMockExamMapper extends EntityMapper<ConfigMockExamDTO, ConfigMockExam> {
}
