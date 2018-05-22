package com.vmcomms.ptemagic.service;

import com.vmcomms.ptemagic.domain.enumeration.QuestionType;
import com.vmcomms.ptemagic.domain.enumeration.SkillType;
import com.vmcomms.ptemagic.service.dto.ExamDTO;
import com.vmcomms.ptemagic.service.dto.ExamTypeDTO;
import com.vmcomms.ptemagic.service.dto.QuestionDTO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Question.
 */
public interface QuestionService {

	List<QuestionDTO> randomQuestion(int numberQuestion);
	
    /**
     * Save a question.
     *
     * @param questionDTO the entity to save
     * @return the persisted entity
     */
    QuestionDTO save(QuestionDTO questionDTO);

    /**
     *  Get all the questions.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<QuestionDTO> findAll(Pageable pageable);
    
    List<QuestionDTO> findAll();

    List<QuestionDTO> findAllBySkill(SkillType skill);
    
    Page<QuestionDTO> findAllBySkillPageable(SkillType skill, Pageable pageable);
    
    List<QuestionDTO> findAllByType(QuestionType type);
    
    /**
     *  Get the "id" question.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    QuestionDTO findOne(Long id);
    
    List<QuestionDTO> findByIdIn(List<Long> ids);

    /**
     *  Delete the "id" question.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
    
    List<QuestionDTO> buildMockTestQuestionExam(ExamTypeDTO examTypeDTO, ExamDTO examDTO);
}
