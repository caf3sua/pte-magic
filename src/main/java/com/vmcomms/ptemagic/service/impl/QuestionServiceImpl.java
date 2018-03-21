package com.vmcomms.ptemagic.service.impl;

import com.vmcomms.ptemagic.service.QuestionService;
import com.vmcomms.ptemagic.domain.Question;
import com.vmcomms.ptemagic.domain.enumeration.QuestionType;
import com.vmcomms.ptemagic.domain.enumeration.SkillType;
import com.vmcomms.ptemagic.repository.QuestionRepository;
import com.vmcomms.ptemagic.service.dto.QuestionDTO;
import com.vmcomms.ptemagic.service.mapper.QuestionMapper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Question.
 */
@Service
@Transactional
public class QuestionServiceImpl implements QuestionService{

    private final Logger log = LoggerFactory.getLogger(QuestionServiceImpl.class);

    private final QuestionRepository questionRepository;

    private final QuestionMapper questionMapper;

    public QuestionServiceImpl(QuestionRepository questionRepository, QuestionMapper questionMapper) {
        this.questionRepository = questionRepository;
        this.questionMapper = questionMapper;
    }

    /**
     * Save a question.
     *
     * @param questionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public QuestionDTO save(QuestionDTO questionDTO) {
        log.debug("Request to save Question : {}", questionDTO);
        Question question = questionMapper.toEntity(questionDTO);
        question = questionRepository.save(question);
        return questionMapper.toDto(question);
    }

    /**
     *  Get all the questions.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<QuestionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Questions");
        return questionRepository.findAll(pageable)
            .map(questionMapper::toDto);
    }

    /**
     *  Get one question by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public QuestionDTO findOne(Long id) {
        log.debug("Request to get Question : {}", id);
        Question question = questionRepository.findOne(id);
        return questionMapper.toDto(question);
    }

    /**
     *  Delete the  question by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Question : {}", id);
        questionRepository.delete(id);
    }

	@Override
	public List<QuestionDTO> randomQuestion(int numberQuestion) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<QuestionDTO> findAll() {
		List<Question> data = questionRepository.findAll();
		return questionMapper.toDto(data);
	}

	@Override
	public List<QuestionDTO> findAllBySkill(SkillType skill) {
		List<QuestionType> lstQuestionEnum = QuestionType.getBySkill(skill.toString());
		List<Question> data = questionRepository.findByTypeIn(lstQuestionEnum);
		return questionMapper.toDto(data);
	}
}
