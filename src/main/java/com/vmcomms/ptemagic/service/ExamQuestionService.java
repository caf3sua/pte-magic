package com.vmcomms.ptemagic.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.vmcomms.ptemagic.service.dto.ExamQuestionDTO;

/**
 * Service Interface for managing ExamQuestion.
 */
public interface ExamQuestionService {

	ExamQuestionDTO save(ExamQuestionDTO answerDTO);

    Page<ExamQuestionDTO> findAll(Pageable pageable);

    ExamQuestionDTO findOne(Long id);

    void delete(Long id);
}
