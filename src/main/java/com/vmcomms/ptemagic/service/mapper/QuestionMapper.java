package com.vmcomms.ptemagic.service.mapper;

import com.vmcomms.ptemagic.domain.*;
import com.vmcomms.ptemagic.service.dto.QuestionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Question and its DTO QuestionDTO.
 */
@Mapper(componentModel = "spring", uses = {ExamMapper.class})
public interface QuestionMapper extends EntityMapper<QuestionDTO, Question> {

    @Mapping(source = "exam.id", target = "examId")
    QuestionDTO toDto(Question question); 

    @Mapping(source = "examId", target = "exam")
    Question toEntity(QuestionDTO questionDTO);

    default Question fromId(Long id) {
        if (id == null) {
            return null;
        }
        Question question = new Question();
        question.setId(id);
        return question;
    }
}
