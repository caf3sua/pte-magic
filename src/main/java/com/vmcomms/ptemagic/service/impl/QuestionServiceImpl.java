package com.vmcomms.ptemagic.service.impl;

import com.vmcomms.ptemagic.service.ConfigMockExamService;
import com.vmcomms.ptemagic.service.ExamQuestionService;
import com.vmcomms.ptemagic.service.QuestionService;
import com.vmcomms.ptemagic.domain.Question;
import com.vmcomms.ptemagic.domain.enumeration.QuestionType;
import com.vmcomms.ptemagic.domain.enumeration.SkillType;
import com.vmcomms.ptemagic.domain.enumeration.TestType;
import com.vmcomms.ptemagic.dto.ConfigMockExamDTO;
import com.vmcomms.ptemagic.dto.QuestionBankInfoDTO;
import com.vmcomms.ptemagic.repository.QuestionRepository;
import com.vmcomms.ptemagic.service.dto.ExamDTO;
import com.vmcomms.ptemagic.service.dto.ExamQuestionDTO;
import com.vmcomms.ptemagic.service.dto.ExamTypeDTO;
import com.vmcomms.ptemagic.service.dto.QuestionDTO;
import com.vmcomms.ptemagic.service.mapper.QuestionMapper;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Question.
 */
@Service
@Transactional
//@CacheConfig(cacheNames = "question")
public class QuestionServiceImpl implements QuestionService{

    private final Logger log = LoggerFactory.getLogger(QuestionServiceImpl.class);

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionMapper questionMapper;
    
    @Autowired
    private ConfigMockExamService configMockExamService;
    
    @Autowired
    private ExamQuestionService examQuestionService;

    /**
     * Save a question.
     *
     * @param questionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    //@CacheEvict
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
//    @Cacheable
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
    //@CacheEvict
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

	@Override
	public List<QuestionDTO> findAllByType(QuestionType type) {
		List<Question> data = questionRepository.findAllByType(type);
		return questionMapper.toDto(data);
	}

	@Override
	public Page<QuestionDTO> findAllByTypePageable(QuestionType type, Pageable pageable) {
		Page<Question> data = questionRepository.findByType(type, pageable);
		return data.map(questionMapper::toDto);
	}
	
	@Override
	public Page<QuestionDTO> findAllBySkillPageable(SkillType skill, Pageable pageable) {
		List<QuestionType> lstQuestionEnum = QuestionType.getBySkill(skill.toString());
		Page<Question> data = questionRepository.findByTypeIn(lstQuestionEnum, pageable);
		return data.map(questionMapper::toDto);
	}

	@Override
	//@Cacheable
	public List<QuestionDTO> findByIdIn(List<Long> ids) {
		List<Question> data = questionRepository.findByIdIn(ids);
		return questionMapper.toDto(data);
	}

	@Override
	//@Cacheable
	public List<QuestionDTO> buildMockTestQuestionExam(ExamTypeDTO examTypeDTO, ExamDTO examDTO) {
		List<QuestionDTO> questions = new ArrayList<>();
    	
    	if (examTypeDTO.getType().equals(TestType.MOCK_TEST_A)) {
    		// Speaking/writing
    		List<QuestionDTO> dataS = configMockExamService.getMockExam(examTypeDTO.getId(), "SPEAKING");
    		questions.addAll(dataS);
    		addTimeBreak(questions, QuestionType.TIME_BREAK);
    		List<QuestionDTO> dataW = configMockExamService.getMockExam(examTypeDTO.getId(), "WRITING");
    		questions.addAll(dataW);
        } else if (examTypeDTO.getType().equals(TestType.MOCK_TEST_B)) {
        	// Reading/listening
        	List<QuestionDTO> dataR = configMockExamService.getMockExam(examTypeDTO.getId(), "READING");
        	questions.addAll(dataR);
    		addTimeBreak(questions, QuestionType.TIME_BREAK);
    		List<QuestionDTO> dataL = configMockExamService.getMockExam(examTypeDTO.getId(), "LISTENING");
    		questions.addAll(dataL);
        } else if (examTypeDTO.getType().equals(TestType.MOCK_TEST_FULL)) {
        	// full
        	List<QuestionDTO> dataS = configMockExamService.getMockExam(examTypeDTO.getId(), "SPEAKING");
        	questions.addAll(dataS);
    		addTimeBreak(questions, QuestionType.TIME_BREAK);
    		List<QuestionDTO> dataW = configMockExamService.getMockExam(examTypeDTO.getId(), "WRITING");
    		questions.addAll(dataW);
    		addTimeBreak(questions, QuestionType.TIME_BREAK);
    		List<QuestionDTO> dataR = configMockExamService.getMockExam(examTypeDTO.getId(), "READING");
    		questions.addAll(dataR);
    		addTimeBreak(questions, QuestionType.TIME_BREAK);
    		List<QuestionDTO> dataL = configMockExamService.getMockExam(examTypeDTO.getId(), "LISTENING");
    		questions.addAll(dataL);
        }
    	
    	int order = 0;
    	List<ExamQuestionDTO> examQuestionDTOs = new ArrayList<>();
		for (QuestionDTO questionDTO : questions) {
			ExamQuestionDTO eqDTO = new ExamQuestionDTO();
			eqDTO.setExamId(examDTO.getId());
			eqDTO.setQuestionId(questionDTO.getId());
			eqDTO.setOrderId(order);
			order++;
			examQuestionDTOs.add(eqDTO);
		}
		// Save
		examQuestionService.save(examQuestionDTOs);
		
		return questions;
	}
	
	private void addTimeBreak(List<QuestionDTO> questions, QuestionType type) {
    	QuestionDTO timeBreak = new QuestionDTO();
    	timeBreak.setType(type);
    	questions.add(timeBreak);
    }

	@Override
	public QuestionBankInfoDTO getQuestionCountInfo() {
		return questionRepository.countQuestionByType();
	}
}
