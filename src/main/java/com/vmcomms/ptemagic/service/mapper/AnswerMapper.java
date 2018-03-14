package com.vmcomms.ptemagic.service.mapper;

import com.vmcomms.ptemagic.domain.*;
import com.vmcomms.ptemagic.service.dto.AnswerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Answer and its DTO AnswerDTO.
 */
@Mapper(componentModel = "spring", uses = {ExamMapper.class})
public interface AnswerMapper extends EntityMapper<AnswerDTO, Answer> {

//    @Mapping(source = "exam.id", target = "examId")
//    AnswerDTO toDto(Answer answer); 
//
//    @Mapping(source = "examId", target = "exam")
//    Answer toEntity(AnswerDTO answerDTO);

    default Answer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Answer answer = new Answer();
        answer.setId(id);
        return answer;
    }
}
