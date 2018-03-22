package com.vmcomms.ptemagic.service.impl;

import com.vmcomms.ptemagic.service.AnswerService;
import com.vmcomms.ptemagic.service.ExamQuestionService;
import com.vmcomms.ptemagic.domain.Answer;
import com.vmcomms.ptemagic.domain.ExamQuestion;
import com.vmcomms.ptemagic.repository.AnswerRepository;
import com.vmcomms.ptemagic.repository.ExamQuestionRepository;
import com.vmcomms.ptemagic.service.dto.AnswerDTO;
import com.vmcomms.ptemagic.service.dto.ExamQuestionDTO;
import com.vmcomms.ptemagic.service.mapper.AnswerMapper;
import com.vmcomms.ptemagic.service.mapper.ExamQuestionMapper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing ExamQuestion.
 */
@Service
@Transactional
public class ExamQuestionServiceImpl implements ExamQuestionService{

    private final Logger log = LoggerFactory.getLogger(ExamQuestionServiceImpl.class);

    @Autowired
    private ExamQuestionRepository examQuestionRepository;

    @Autowired
    private ExamQuestionMapper examQuestionMapper;


    /**
     * Save a answer.
     *
     * @param answerDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ExamQuestionDTO save(ExamQuestionDTO examQuestionDTO) {
        log.debug("Request to save ExamQuestion : {}", examQuestionDTO);
        ExamQuestion answer = examQuestionMapper.toEntity(examQuestionDTO);
        answer = examQuestionRepository.save(answer);
        return examQuestionMapper.toDto(answer);
    }

    /**
     *  Get all the answers.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ExamQuestionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Answers");
        return examQuestionRepository.findAll(pageable)
            .map(examQuestionMapper::toDto);
    }

    /**
     *  Get one answer by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public ExamQuestionDTO findOne(Long id) {
        log.debug("Request to get ExamQuestion : {}", id);
        ExamQuestion answer = examQuestionRepository.findOne(id);
        return examQuestionMapper.toDto(answer);
    }

    /**
     *  Delete the  answer by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Answer : {}", id);
        examQuestionRepository.delete(id);
    }

	@Override
	public List<ExamQuestionDTO> findAllByExamId(Long examId) {
		log.debug("Request to findAllByExamId : {}", examId);
        List<ExamQuestion> data = examQuestionRepository.findAllByExamId(examId);
        return examQuestionMapper.toDto(data);
	}
}
