package com.vmcomms.ptemagic.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
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
import com.vmcomms.ptemagic.domain.User;
import com.vmcomms.ptemagic.domain.enumeration.ProgressType;
import com.vmcomms.ptemagic.domain.enumeration.QuestionType;
import com.vmcomms.ptemagic.domain.enumeration.SkillType;
import com.vmcomms.ptemagic.domain.enumeration.TestType;
import com.vmcomms.ptemagic.dto.AnswerQuestionDTO;
import com.vmcomms.ptemagic.dto.ExamInfoDTO;
import com.vmcomms.ptemagic.service.AnswerService;
import com.vmcomms.ptemagic.service.ExamQuestionService;
import com.vmcomms.ptemagic.service.ExamService;
import com.vmcomms.ptemagic.service.ExamTypeService;
import com.vmcomms.ptemagic.service.MarkScoreService;
import com.vmcomms.ptemagic.service.QuestionService;
import com.vmcomms.ptemagic.service.UserService;
import com.vmcomms.ptemagic.service.dto.AnswerDTO;
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
    private AnswerService answerService;
    
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
    
    @PostMapping("/finish-marking-exam")
    @Timed
    public ResponseEntity<Void> finishMarkingExam(@RequestBody ExamVM examVM) throws URISyntaxException {
        log.debug("REST request to finish marking Exam : {}", examVM);
        
        // Check input
        if (examVM.getId() == null) {
            throw new BadRequestAlertException("A exam cannot null ID", ENTITY_NAME, "idexists");
        }
        
        // Call service
        markScoreService.finishExamByMarking(examVM.getId());
        
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
        
        ExamTypeDTO examTypeDTO = examTypeService.findOne(examDTO.getExamTypeId());
        List<QuestionDTO> question = null;
        /* Generate exam question */
        // , MOCK_TEST_A, MOCK_TEST_B, MOCK_TEST_FULL
        if (examTypeDTO.getType().equals(TestType.MOCK_TEST_A) || examTypeDTO.getType().equals(TestType.MOCK_TEST_B) || 
        		examTypeDTO.getType().equals(TestType.MOCK_TEST_FULL)) {
        	question = generateMockTestQuestionExam(result);
        } else {
            // Random to create/choice question for exam
            question = generateQuestionExam(result);
        }
        
        
        ExamInfoDTO examInfoDTO = new ExamInfoDTO();
        examInfoDTO.setExamDTO(result);
        examInfoDTO.setQuestions(question);
        examInfoDTO.setExamTypeDTO(examTypeService.findOne(examVM.getExamTypeId()));
        calculateNumberQuestion(examInfoDTO);
        
        return ResponseEntity.created(new URI("/api/exams/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(examInfoDTO);
    }
    
    private void calculateNumberQuestion(ExamInfoDTO examInfoDTO) {
    	for (QuestionDTO q : examInfoDTO.getQuestions()) {
			if (q.getType().toString().contains("SPEAKING")) {
				examInfoDTO.setNumberQuestionSpeaking(examInfoDTO.getNumberQuestionSpeaking() + 1);
			} else if (q.getType().toString().contains("WRITING")) {
				examInfoDTO.setNumberQuestionWriting(examInfoDTO.getNumberQuestionWriting() + 1);
			} else if (q.getType().toString().contains("READING")) {
				examInfoDTO.setNumberQuestionReading(examInfoDTO.getNumberQuestionReading() + 1);
			} else if (q.getType().toString().contains("LISTENING")) {
				examInfoDTO.setNumberQuestionListening(examInfoDTO.getNumberQuestionListening() + 1);
			}
		}
    }

    private List<QuestionDTO> generateMockTestQuestionExam(ExamDTO examDTO) {
    	ExamTypeDTO examTypeDTO = examTypeService.findOne(examDTO.getExamTypeId());
    	List<QuestionDTO> questions = new ArrayList<>();
    	
    	if (examTypeDTO.getType().equals(TestType.MOCK_TEST_A)) {
    		// Speaking/writing
    		selectMockTestExamQuestionSpeaking(examTypeDTO, questions);
    		addTimeBreak(questions, QuestionType.TIME_BREAK);
    		selectMockTestExamQuestionWriting(examTypeDTO, questions);
        } else if (examTypeDTO.getType().equals(TestType.MOCK_TEST_B)) {
        	// Reading/listening
        	selectMockTestExamQuestionReading(examTypeDTO, questions);
    		addTimeBreak(questions, QuestionType.TIME_BREAK);
        	selectMockTestExamQuestionListening(examTypeDTO, questions);
        } else if (examTypeDTO.getType().equals(TestType.MOCK_TEST_FULL)) {
        	// full
        	selectMockTestExamQuestionSpeaking(examTypeDTO, questions);
        	addTimeBreak(questions, QuestionType.TIME_BREAK);
    		selectMockTestExamQuestionWriting(examTypeDTO, questions);
    		addTimeBreak(questions, QuestionType.TIME_BREAK);
    		selectMockTestExamQuestionReading(examTypeDTO, questions);
    		addTimeBreak(questions, QuestionType.TIME_BREAK);
        	selectMockTestExamQuestionListening(examTypeDTO, questions);
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
    
    private List<QuestionDTO> generateQuestionExam(ExamDTO examDTO) {
    	ExamTypeDTO examTypeDTO = examTypeService.findOne(examDTO.getExamTypeId());
    	List<QuestionDTO> questions = new ArrayList<>();
    	
    	selectExamQuestion(examTypeDTO, questions);
    	
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
    
    private void selectExamQuestion(ExamTypeDTO examTypeDTO, List<QuestionDTO> questions) {
    	// Select question
    	if (examTypeDTO.getNumQuestion1() != null && examTypeDTO.getNumQuestion1() > 0) {
    		selectQuestionByType(questions, examTypeDTO.getNumQuestion1(), QuestionType.SPEAKING_READ_ALOUD);
    	}
    	if (examTypeDTO.getNumQuestion2() != null && examTypeDTO.getNumQuestion2() > 0) {
    		selectQuestionByType(questions, examTypeDTO.getNumQuestion2(), QuestionType.SPEAKING_REPEAT_SENTENCE);
    	}
    	if (examTypeDTO.getNumQuestion3() != null && examTypeDTO.getNumQuestion3() > 0) {
    		selectQuestionByType(questions, examTypeDTO.getNumQuestion3(), QuestionType.SPEAKING_DESCRIBE_IMAGE);
    	}
    	if (examTypeDTO.getNumQuestion4() != null && examTypeDTO.getNumQuestion4() > 0) {
    		selectQuestionByType(questions, examTypeDTO.getNumQuestion4(), QuestionType.SPEAKING_RETELL_LECTURE);
    	}
    	if (examTypeDTO.getNumQuestion5() != null && examTypeDTO.getNumQuestion5() > 0) {
    		selectQuestionByType(questions, examTypeDTO.getNumQuestion5(), QuestionType.SPEAKING_ANSWER_SHORT_QUESTION);
    	}
    	if (examTypeDTO.getNumQuestion6() != null && examTypeDTO.getNumQuestion6() > 0) {
    		selectQuestionByType(questions, examTypeDTO.getNumQuestion6(), QuestionType.WRITING_SUMMARIZE_WRITTEN_TEXT);
    	}
    	if (examTypeDTO.getNumQuestion7() != null && examTypeDTO.getNumQuestion7() > 0) {
    		selectQuestionByType(questions, examTypeDTO.getNumQuestion7(), QuestionType.WRITING_ESSAY);
    	}
    	if (examTypeDTO.getNumQuestion8() != null && examTypeDTO.getNumQuestion8() > 0) {
    		selectQuestionByType(questions, examTypeDTO.getNumQuestion8(), QuestionType.READING_FIB_R_W);
    	}
    	if (examTypeDTO.getNumQuestion9() != null && examTypeDTO.getNumQuestion9() > 0) {
    		selectQuestionByType(questions, examTypeDTO.getNumQuestion9(), QuestionType.READING_FIB_R);
    	}
    	if (examTypeDTO.getNumQuestion10() != null && examTypeDTO.getNumQuestion10() > 0) {
    		selectQuestionByType(questions, examTypeDTO.getNumQuestion10(), QuestionType.READING_RE_ORDER_PARAGRAPH);
    	}
    	if (examTypeDTO.getNumQuestion11() != null && examTypeDTO.getNumQuestion11() > 0) {
    		selectQuestionByType(questions, examTypeDTO.getNumQuestion11(), QuestionType.READING_MCQ_R_SINGLE_ANSWER);
    	}
    	if (examTypeDTO.getNumQuestion12() != null && examTypeDTO.getNumQuestion12() > 0) {
    		selectQuestionByType(questions, examTypeDTO.getNumQuestion12(), QuestionType.READING_MCQ_R_MULTIPLE_ANSWER);
    	}
    	if (examTypeDTO.getNumQuestion13() != null && examTypeDTO.getNumQuestion13() > 0) {
    		selectQuestionByType(questions, examTypeDTO.getNumQuestion13(), QuestionType.LISTENING_SUMMARIZE_SPOKEN_TEXT);
    	}
    	if (examTypeDTO.getNumQuestion14() != null && examTypeDTO.getNumQuestion14() > 0) {
    		selectQuestionByType(questions, examTypeDTO.getNumQuestion14(), QuestionType.LISTENING_FIB_L);
    	}
    	if (examTypeDTO.getNumQuestion15() != null && examTypeDTO.getNumQuestion15() > 0) {
    		selectQuestionByType(questions, examTypeDTO.getNumQuestion15(), QuestionType.LISTENING_MCQ_L_SINGLE_ANSWER);
    	}
    	if (examTypeDTO.getNumQuestion16() != null && examTypeDTO.getNumQuestion16() > 0) {
    		selectQuestionByType(questions, examTypeDTO.getNumQuestion16(), QuestionType.LISTENING_MCQ_L_MULTIPLE_ANSWER);
    	}
    	if (examTypeDTO.getNumQuestion17() != null && examTypeDTO.getNumQuestion17() > 0) {
    		selectQuestionByType(questions, examTypeDTO.getNumQuestion17(), QuestionType.LISTENING_HIGHLIGHT_CORRECT_SUMMARY);
    	}
    	if (examTypeDTO.getNumQuestion18() != null && examTypeDTO.getNumQuestion18() > 0) {
    		selectQuestionByType(questions, examTypeDTO.getNumQuestion18(), QuestionType.LISTENING_SELECT_MISSING_WORD);
    	}
    	if (examTypeDTO.getNumQuestion19() != null && examTypeDTO.getNumQuestion19() > 0) {
    		selectQuestionByType(questions, examTypeDTO.getNumQuestion19(), QuestionType.LISTENING_HIGHLIGHT_INCORRECT_WORD);
    	}
    	if (examTypeDTO.getNumQuestion20() != null && examTypeDTO.getNumQuestion20() > 0) {
    		selectQuestionByType(questions, examTypeDTO.getNumQuestion20(), QuestionType.LISTENING_DICTATION);
    	}
    }
    
    
    private void selectMockTestExamQuestionListening(ExamTypeDTO examTypeDTO, List<QuestionDTO> questions) {
    	// Select question listening
		//    	13. Summarize Spoken Text (3)
		//    	14. Fill in the Blank (2)
		//    	15. MCQ Single Answer (2)
		//    	16. MCQs Multiple Answer (2)
		//    	17. Highlight Correct Summary (2)
		//    	18. Select Missing Words (2)
		//    	19. Highlight Incorrect Words (2)
		//    	20. Dictation (4)
    		selectQuestionByType(questions, 3, QuestionType.LISTENING_SUMMARIZE_SPOKEN_TEXT);
    		selectQuestionByType(questions, 2, QuestionType.LISTENING_FIB_L);
    		selectQuestionByType(questions, 2, QuestionType.LISTENING_MCQ_L_SINGLE_ANSWER);
    		selectQuestionByType(questions, 2, QuestionType.LISTENING_MCQ_L_MULTIPLE_ANSWER);
    		selectQuestionByType(questions, 2, QuestionType.LISTENING_HIGHLIGHT_CORRECT_SUMMARY);
    		selectQuestionByType(questions, 2, QuestionType.LISTENING_SELECT_MISSING_WORD);
    		selectQuestionByType(questions, 2, QuestionType.LISTENING_HIGHLIGHT_INCORRECT_WORD);
    		selectQuestionByType(questions, 4, QuestionType.LISTENING_DICTATION);
    		
    	// DUMP
//    		selectQuestionByType(questions, 3, QuestionType.LISTENING_SUMMARIZE_SPOKEN_TEXT);
//    		selectQuestionByType(questions, 2, QuestionType.LISTENING_FIB_L);
    }
    
    private void selectMockTestExamQuestionReading(ExamTypeDTO examTypeDTO, List<QuestionDTO> questions) {
    	// Select question reading
		//    	8. Fill in the Blank: R+W (7)
		//    	9. Fill in the Blank: R (5)
		//    	10. Re-order Paragraph (3)
		//    	11. MCQ Single Answer (2)
		//    	12. MCQ Multiple Answer (3)
		selectQuestionByType(questions, 7, QuestionType.READING_FIB_R_W);
		selectQuestionByType(questions, 5, QuestionType.READING_FIB_R);
		selectQuestionByType(questions, 3, QuestionType.READING_RE_ORDER_PARAGRAPH);
		selectQuestionByType(questions, 2, QuestionType.READING_MCQ_R_SINGLE_ANSWER);
		selectQuestionByType(questions, 3, QuestionType.READING_MCQ_R_MULTIPLE_ANSWER);

    	// DUMP
//		selectQuestionByType(questions, 2, QuestionType.READING_FIB_R_W);
    }
    
    private void selectMockTestExamQuestionWriting(ExamTypeDTO examTypeDTO, List<QuestionDTO> questions) {
    	// Select question writing
		//    	6. Summarize Written Text (3)
		//    	7. Essay (1-2)
		selectQuestionByType(questions, 3, QuestionType.WRITING_SUMMARIZE_WRITTEN_TEXT);
		selectQuestionByType(questions, 1, QuestionType.WRITING_ESSAY);
    }
    
    private void selectMockTestExamQuestionSpeaking(ExamTypeDTO examTypeDTO, List<QuestionDTO> questions) {
    	// Select question Speaking
		//    	1. Read Aloud (6)
		//    	2. Repeat Sentence (10)
		//    	3. Describe Image (6)
		//    	4. Retell Lecture (4)
		//    	5. Answer Short Question (10)
		selectQuestionByType(questions, 6, QuestionType.SPEAKING_READ_ALOUD);
		selectQuestionByType(questions, 10, QuestionType.SPEAKING_REPEAT_SENTENCE);
		selectQuestionByType(questions, 6, QuestionType.SPEAKING_DESCRIBE_IMAGE);
		selectQuestionByType(questions, 4, QuestionType.SPEAKING_RETELL_LECTURE);
		selectQuestionByType(questions, 10, QuestionType.SPEAKING_ANSWER_SHORT_QUESTION);
		
		// DUMP
//    	selectQuestionByType(questions, 1, QuestionType.SPEAKING_READ_ALOUD);
//		selectQuestionByType(questions, 1, QuestionType.SPEAKING_REPEAT_SENTENCE);
//		selectQuestionByType(questions, 1, QuestionType.SPEAKING_DESCRIBE_IMAGE);
//		selectQuestionByType(questions, 1, QuestionType.SPEAKING_RETELL_LECTURE);
//		selectQuestionByType(questions, 1, QuestionType.SPEAKING_ANSWER_SHORT_QUESTION);
    }
    
    private void addTimeBreak(List<QuestionDTO> questions, QuestionType type) {
    	QuestionDTO timeBreak = new QuestionDTO();
    	timeBreak.setType(type);
    	questions.add(timeBreak);
    }
    
    private void selectQuestionByType(List<QuestionDTO> questions, int number, QuestionType type) {
    	List<QuestionDTO> questionDTOs = questionService.findAllByType(type);
    	// sub 
    	if (questionDTOs != null && questionDTOs.size() > number) {
    		Collections.shuffle(questionDTOs);
    		 List<QuestionDTO> data = questionDTOs.subList(0, number);
    		 questions.addAll(data);
    		 return;
    	}
    	
    	questions.addAll(questionDTOs);
    }
    
    private SkillType getSkillTypeByExamType(ExamTypeDTO examTypeDTO) {
    	String testType = examTypeDTO.getType().toString();
    	
    	String skillTypeStr = testType.substring(testType.lastIndexOf("_") + 1);
    	return SkillType.valueOf(skillTypeStr);
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
        Page<ExamDTO> page = examService.findAllByResult(pageable, ProgressType.MARKING);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/exams");
        
        // Get email
        for (ExamDTO examDTO : page.getContent()) {
			// Find user
        	User user = userService.getUserWithAuthorities(examDTO.getUserId());
        	examDTO.setEmail(user.getEmail());
        	ExamTypeDTO examTypeDTO = examTypeService.findOne(examDTO.getExamTypeId());
        	if (null != examTypeDTO) {
        		examDTO.setExamTypeName(examTypeDTO.getName());
        	}
		}
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
    public ResponseEntity<ExamInfoDTO> getExam(@PathVariable Long id) {
        log.debug("REST request to get Exam : {}", id);
        ExamInfoDTO examInfoDTO = new ExamInfoDTO();
        List<AnswerQuestionDTO> lstAnswerQuestion = new ArrayList<>();
        examInfoDTO.setAnswerQuestions(lstAnswerQuestion);
        
        // Get examDTO
        ExamDTO examDTO = examService.findOne(id);
        examInfoDTO.setExamDTO(examDTO);
        
        ExamTypeDTO examTypeDTO = examTypeService.findOne(examDTO.getExamTypeId());
        examInfoDTO.setExamTypeDTO(examTypeDTO);
        
        // User
        User user = userService.getUserWithAuthorities(examDTO.getUserId());
    	examDTO.setEmail(user.getEmail());
    	
        // Get all question 
		// Get list exam question
		List<ExamQuestionDTO> examQuestions = examQuestionService.findAllByExamId(id);

		for (ExamQuestionDTO examQuestionDTO : examQuestions) {
			AnswerQuestionDTO item = new AnswerQuestionDTO();
			// Get question to compare
			if (examQuestionDTO.getQuestionId() != null) {
				QuestionDTO questionDTO = questionService.findOne(examQuestionDTO.getQuestionId());
				item.setQuestion(questionDTO);
			}
			
			// Get answer
			AnswerDTO answerDTO = answerService.findOneByExamIdAndQuestionId(id, examQuestionDTO.getQuestionId());
			
			item.setAnswer(answerDTO);
			lstAnswerQuestion.add(item);
		}
        
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(examInfoDTO));
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
        ExamDTO examDTO = examService.findOne(id);
        examDTO.setResult(ProgressType.DONE);
        examService.save(examDTO);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
