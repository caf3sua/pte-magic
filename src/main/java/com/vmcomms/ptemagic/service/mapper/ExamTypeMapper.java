package com.vmcomms.ptemagic.service.mapper;

import com.vmcomms.ptemagic.domain.*;
import com.vmcomms.ptemagic.service.dto.ExamTypeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ExamType and its DTO ExamTypeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ExamTypeMapper extends EntityMapper<ExamTypeDTO, ExamType> {

    

    

    default ExamType fromId(Long id) {
        if (id == null) {
            return null;
        }
        ExamType examType = new ExamType();
        examType.setId(id);
        return examType;
    }
}
