package com.vmcomms.ptemagic.service;

import com.vmcomms.ptemagic.service.dto.AnswerDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Answer.
 */
public interface AnswerService {

    AnswerDTO findOneByExamIdAndQuestionId(Long examId, Long questionId);
    /**
     * Save a answer.
     *
     * @param answerDTO the entity to save
     * @return the persisted entity
     */
    AnswerDTO save(AnswerDTO answerDTO);

    /**
     *  Get all the answers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<AnswerDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" answer.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    AnswerDTO findOne(Long id);

    /**
     *  Delete the "id" answer.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);
}
