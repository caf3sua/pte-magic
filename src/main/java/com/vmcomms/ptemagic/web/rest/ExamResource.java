package com.vmcomms.ptemagic.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.vmcomms.ptemagic.domain.enumeration.ProgressType;
import com.vmcomms.ptemagic.domain.enumeration.SkillType;
import com.vmcomms.ptemagic.dto.ExamInfoDTO;
import com.vmcomms.ptemagic.service.ExamQuestionService;
import com.vmcomms.ptemagic.service.ExamService;
import com.vmcomms.ptemagic.service.ExamTypeService;
import com.vmcomms.ptemagic.service.MarkScoreService;
import com.vmcomms.ptemagic.service.QuestionService;
import com.vmcomms.ptemagic.service.UserService;
import com.vmcomms.ptemagic.service.dto.ExamDTO;
import com.vmcomms.ptemagic.service.dto.ExamQuestionDTO;
import com.vmcomms.ptemagic.service.dto.ExamTypeDTO;
import com.vmcomms.ptemagic.service.dto.QuestionDTO;
import com.vmcomms.ptemagic.service.dto.UserDTO;
import com.vmcomms.ptemagic.web.rest.errors.BadRequestAlertException;
import com.vmcomms.ptemagic.web.rest.errors.InternalServerErrorException;
import com.vmcomms.ptemagic.web.rest.util.HeaderUtil;
import com.vmcomms.ptemagic.web.rest.util.PaginationUtil;
import com.vmcomms.ptemagic.web.rest.vm.ExamVM;

import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

/**
 * REST controller for managing Exam.
 */
@RestController
@RequestMapping("/api")
public class ExamResource {

    private final Logger log = LoggerFactory.getLogger(ExamResource.class);

    private static final String ENTITY_NAME = "exam";

    @Autowired
    private ExamQuestionService examQuestionService;
    
    @Autowired
    private ExamService examService;

    @Autowired
    private ExamTypeService examTypeService;

    @Autowired
    private QuestionService questionService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private MarkScoreService markScoreService;
    
    @PostMapping("/finish-exam")
    @Timed
    public ResponseEntity<Void> finishExam(@RequestBody ExamVM examVM) throws URISyntaxException {
        log.debug("REST request to finish Exam : {}", examVM);
        
        // Check input
        if (examVM.getId() == null) {
            throw new BadRequestAlertException("A exam cannot null ID", ENTITY_NAME, "idexists");
        }
        
        // Call service
        markScoreService.markScore(examVM.getId());
        
        return ResponseEntity.ok().headers(HeaderUtil. createEntityUpdateAlert(ENTITY_NAME, examVM.getId().toString())).build();
    }
    
    /**
     * POST  /exams : Create a new exam.
     *
     * @param examDTO the examDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new examDTO, or with status 400 (Bad Request) if the exam has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/start-exam")
    @Timed
    public ResponseEntity<ExamInfoDTO> startExam(@RequestBody ExamVM examVM) throws URISyntaxException {
        log.debug("REST request to start Exam : {}", examVM);
        
        // User info
        UserDTO userDTO = Optional.ofNullable(userService.getUserWithAuthorities())
                .map(UserDTO::new)
                .orElseThrow(() -> new InternalServerErrorException("User could not be found"));
        
        if (examVM.getExamTypeId() == null) {
            throw new BadRequestAlertException("A new exam cannot start by exam type null", ENTITY_NAME, "idexists");
        }
        
        ExamDTO examDTO = new ExamDTO();
        examDTO.setUserId(userDTO.getId());
        examDTO.setResult(ProgressType.INPROGRESS);
        examDTO.setExamTypeId(examVM.getExamTypeId());
         
        ExamDTO result = examService.save(examDTO);
        
        // Random to create/choice question for exam
        List<QuestionDTO> question = generateQuestionExam(result);
        
        
        ExamInfoDTO examInfoDTO = new ExamInfoDTO();
        examInfoDTO.setExamDTO(result);
        examInfoDTO.setQuestions(question);
        
        return ResponseEntity.created(new URI("/api/exams/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(examInfoDTO);
    }

    
    private List<QuestionDTO> generateQuestionExam(ExamDTO examDTO) {
    	/** 3 - READING **/
        /** 4 - LISTENING **/
    	ExamTypeDTO examTypeDTO = examTypeService.findOne(examDTO.getExamTypeId());
    	Integer numReading = examTypeDTO.getNumberQuestionReading();
    	Integer numListening = examTypeDTO.getNumberQuestionListening();
    	Integer numWriting = examTypeDTO.getNumberQuestionWriting();
    	Integer numSpeaking = examTypeDTO.getNumberQuestionSpeaking();
    	
    	List<QuestionDTO> questions = new ArrayList<>();
    	
    	// Reading
    	if (numReading != null && numReading > 0) {
    		selectQuestionBySkill(questions, numReading, SkillType.READING);
    	}

    	// Listening
    	if (numListening != null && numListening > 0) {
    		selectQuestionBySkill(questions, numListening, SkillType.LISTENING);
    	}
    	
    	// Writing
    	if (numWriting != null && numWriting > 0) {
    		selectQuestionBySkill(questions, numWriting, SkillType.WRITING);
    	}
    	
    	// Speaking
    	if (numSpeaking != null && numSpeaking > 0) {
    		selectQuestionBySkill(questions, numSpeaking, SkillType.SPEAKING);
    	}
    	
    	int order = 0;
		for (QuestionDTO questionDTO : questions) {
			ExamQuestionDTO eqDTO = new ExamQuestionDTO();
			eqDTO.setExamId(examDTO.getId());
			eqDTO.setQuestionId(questionDTO.getId());
			eqDTO.setOrderId(order);
			examQuestionService.save(eqDTO);
			order++;
		}
		
		return questions;
    }
    
    private void selectQuestionBySkill(List<QuestionDTO> questions, int number, SkillType skill) {
    	List<QuestionDTO> questionDTOs = questionService.findAllBySkill(skill);
    	// sub 
    	if (questionDTOs != null && questionDTOs.size() > number) {
    		Collections.shuffle(questionDTOs);
    		 List<QuestionDTO> data = questionDTOs.subList(0, number);
    		 questions.addAll(data);
    		 return;
    	}
    	
    	questions.addAll(questionDTOs);
    }
    
    @PostMapping("/exams/resume")
    @Timed
    public ResponseEntity<ExamInfoDTO> resumeExam(@RequestBody ExamVM examVM) throws URISyntaxException {
        log.debug("REST request to start Exam : {}", examVM);
        
//        UserDTO userDTO = Optional.ofNullable(userService.getUserWithAuthorities())
//                .map(UserDTO::new)
//                .orElseThrow(() -> new InternalServerErrorException("User could not be found"));
//        
//        if (examVM.getExamTypeId() != null) {
//            throw new BadRequestAlertException("A new exam cannot already have an ID", ENTITY_NAME, "idexists");
//        }
//        
//        ExamDTO examDTO = new ExamDTO();
//        examDTO.setUserId(userDTO.getId());
//        examDTO.setResult(ProgressType.INPROGRESS);
//        examDTO.setExamTypeId(examVM.getExamTypeId());
//         
//        ExamDTO result = examService.save(examDTO);
//        
//        // Random to create/choice question for exam
//        List<QuestionDTO> question = generateQuestionExam(examDTO);
        
        
        ExamInfoDTO examInfoDTO = new ExamInfoDTO();
//        examInfoDTO.setExamDTO(examDTO);
//        examInfoDTO.setQuestions(question);
        
        return ResponseEntity.created(new URI("/api/exams/" + examVM.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, examVM.getId().toString()))
            .body(examInfoDTO);
    }
    
    @PostMapping("/exams/cancel")
    @Timed
    public ResponseEntity<Void> cancelExam(@RequestBody ExamDTO examDTO) throws URISyntaxException {
        log.debug("REST request to cancel Exam : {}", examDTO);

        examService.delete(examDTO.getId());
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, examDTO.getId().toString())).build();
    }
    
    /**
     * PUT  /exams : Updates an existing exam.
     *
     * @param examDTO the examDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated examDTO,
     * or with status 400 (Bad Request) if the examDTO is not valid,
     * or with status 500 (Internal Server Error) if the examDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/exams")
    @Timed
    public ResponseEntity<ExamDTO> updateExam(@RequestBody ExamDTO examDTO) throws URISyntaxException {
        log.debug("REST request to update Exam : {}", examDTO);
//        if (examDTO.getId() == null) {
//            return createExam(examDTO);
//        }
        ExamDTO result = examService.save(examDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, examDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /exams : get all the exams.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of exams in body
     */
    @GetMapping("/exams")
    @Timed
    public ResponseEntity<List<ExamDTO>> getAllExams(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Exams");
        Page<ExamDTO> page = examService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/exams");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /exams/:id : get the "id" exam.
     *
     * @param id the id of the examDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the examDTO, or with status 404 (Not Found)
     */
    @GetMapping("/exams/{id}")
    @Timed
    public ResponseEntity<ExamDTO> getExam(@PathVariable Long id) {
        log.debug("REST request to get Exam : {}", id);
        ExamDTO examDTO = examService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(examDTO));
    }

    /**
     * DELETE  /exams/:id : delete the "id" exam.
     *
     * @param id the id of the examDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/exams/{id}")
    @Timed
    public ResponseEntity<Void> deleteExam(@PathVariable Long id) {
        log.debug("REST request to delete Exam : {}", id);
        examService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
