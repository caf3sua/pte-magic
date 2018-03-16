package com.vmcomms.ptemagic.dto;

import java.util.List;

import com.vmcomms.ptemagic.service.dto.ExamDTO;
import com.vmcomms.ptemagic.service.dto.QuestionDTO;

import lombok.Getter;
import lombok.Setter;


public class ExamInfoDTO {

	private ExamDTO examDTO;

	private List<QuestionDTO> questions;

    public ExamDTO getExamDTO() {
        return examDTO;
    }

    public void setExamDTO(ExamDTO examDTO) {
        this.examDTO = examDTO;
    }

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }
}
