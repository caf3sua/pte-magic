package com.vmcomms.ptemagic.service.mapper;

import com.vmcomms.ptemagic.domain.*;
import com.vmcomms.ptemagic.service.dto.ExamDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Exam and its DTO ExamDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ExamMapper extends EntityMapper<ExamDTO, Exam> {


    default Exam fromId(Long id) {
        if (id == null) {
            return null;
        }
        Exam exam = new Exam();
        exam.setId(id);
        return exam;
    }
}
