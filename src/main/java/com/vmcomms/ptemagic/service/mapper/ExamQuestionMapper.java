package com.vmcomms.ptemagic.service.mapper;

import org.mapstruct.Mapper;

import com.vmcomms.ptemagic.domain.ExamQuestion;
import com.vmcomms.ptemagic.service.dto.ExamQuestionDTO;

/**
 * Mapper for the entity ExamQuestion and its DTO ExamQuestionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ExamQuestionMapper extends EntityMapper<ExamQuestionDTO, ExamQuestion> {


    default ExamQuestion fromId(Long id) {
        if (id == null) {
            return null;
        }
        ExamQuestion examQuestion = new ExamQuestion();
        examQuestion.setId(id);
        return examQuestion;
    }
}
