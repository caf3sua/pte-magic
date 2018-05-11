package com.vmcomms.ptemagic.service.mapper;

import org.mapstruct.Mapper;

import com.vmcomms.ptemagic.domain.UserLimitExam;
import com.vmcomms.ptemagic.dto.UserLimitExamDTO;

@Mapper(componentModel = "spring", uses = {})
public interface UserLimitExamMapper extends EntityMapper<UserLimitExamDTO, UserLimitExam> {
}
