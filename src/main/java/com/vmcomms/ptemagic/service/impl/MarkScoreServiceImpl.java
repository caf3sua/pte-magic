package com.vmcomms.ptemagic.service.impl;

import com.vmcomms.ptemagic.service.AnswerService;
import com.vmcomms.ptemagic.service.ExamQuestionService;
import com.vmcomms.ptemagic.service.ExamService;
import com.vmcomms.ptemagic.service.ExamTypeService;
import com.vmcomms.ptemagic.service.MailService;
import com.vmcomms.ptemagic.service.MarkScoreService;
import com.vmcomms.ptemagic.service.QuestionService;
import com.vmcomms.ptemagic.service.UserService;
import com.vmcomms.ptemagic.domain.Exam;
import com.vmcomms.ptemagic.domain.enumeration.ProgressType;
import com.vmcomms.ptemagic.domain.enumeration.TestType;
import com.vmcomms.ptemagic.dto.ScoreInfoDTO;
import com.vmcomms.ptemagic.repository.ExamRepository;
import com.vmcomms.ptemagic.service.dto.AnswerDTO;
import com.vmcomms.ptemagic.service.dto.ExamDTO;
import com.vmcomms.ptemagic.service.dto.ExamQuestionDTO;
import com.vmcomms.ptemagic.service.dto.ExamTypeDTO;
import com.vmcomms.ptemagic.service.dto.QuestionDTO;
import com.vmcomms.ptemagic.service.dto.UserDTO;
import com.vmcomms.ptemagic.service.mapper.ExamMapper;
import com.vmcomms.ptemagic.web.rest.errors.InternalServerErrorException;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Exam.
 */
@Service
@Transactional
public class MarkScoreServiceImpl implements MarkScoreService {

    private final Logger log = LoggerFactory.getLogger(MarkScoreServiceImpl.class);

    @Autowired
    private UserService userService;
    
    @Autowired
    private MailService mailService;
    
    @Autowired
    private ExamService examService;
    
    @Autowired
    private ExamQuestionService examQuestionService;
    
    @Autowired
    private AnswerService answerService;
    
    @Autowired
    private ExamTypeService examTypeService;
    
    @Autowired
    private QuestionService questionService;
    
    private final static Long MOCK_TEST_A_EXAM_TYPE_ID = 2001l;
    private final static Long MOCK_TEST_B_EXAM_TYPE_ID = 2002l;
    private final static Long MOCK_TEST_FULL_EXAM_TYPE_ID = 2003l;
    
	@Override
	public void markScore(Long examId) {
		// User info
        UserDTO userDTO = Optional.ofNullable(userService.getUserWithAuthorities())
                .map(UserDTO::new)
                .orElseThrow(() -> new InternalServerErrorException("User could not be found"));
        
		// Get list exam question
		List<ExamQuestionDTO> examQuestions = examQuestionService.findAllByExamId(examId);

        // Calculate score
		int totalScore = examQuestions.size();
		int score = 0;
		// Compare
		for (ExamQuestionDTO examQuestionDTO : examQuestions) {
			// Get question to compare
			QuestionDTO questionDTO = questionService.findOne(examQuestionDTO.getQuestionId());
			// Get answer
			AnswerDTO answerDTO = answerService.findOneByExamIdAndQuestionId(examId, examQuestionDTO.getQuestionId());
			if (StringUtils.equals(questionDTO.getExpectAnswer(), answerDTO.getAnswer())) {
				score++;
			}
		}

		log.debug("score of user : {}/{}", score, totalScore);

		// Update Exam -> ProgressType.DONE
		ExamDTO examDTO = examService.findOne(examId);
		
		// Check finish
		if (isFinishExam(examDTO)) {
			ExamTypeDTO examTypeDTO = examTypeService.findOne(examDTO.getExamTypeId());
			
			ScoreInfoDTO scoreInfo = new ScoreInfoDTO();
			scoreInfo.setUser(userDTO);
			scoreInfo.setScore(score);
			scoreInfo.setExamTitle(examTypeDTO.getName());
			scoreInfo.setTotalQuestion(totalScore);
			
	        // Send mail
			mailService.sendScoreEmail(scoreInfo);
			
			// Update -> DONE
			examDTO.setResult(ProgressType.DONE);
			examService.save(examDTO);
		} else {
			examDTO.setResult(ProgressType.MARKING);
			examService.save(examDTO);
		}
	}

	private boolean isFinishExam(ExamDTO examDTO) {
		if (isMockTest(examDTO)) {
			return false;
		}
		
		ExamTypeDTO examTypeDTO = examTypeService.findOne(examDTO.getExamTypeId());
		// Finish : FREE_EXAM
		if (examTypeDTO.getType() == TestType.FREE_EXAM_LISTENING || examTypeDTO.getType() == TestType.FREE_EXAM_READING) {
			return true;
		}
		
		// Finish : MEMBER_QUESTION (reading/listening)
		if (examTypeDTO.getType() == TestType.MEMBER_QUESTION_LISTENING || examTypeDTO.getType() == TestType.MEMBER_QUESTION_READING) {
			return true;
		}
		
		return false;
	}
	
	private boolean isMockTest(ExamDTO examDTO) {
		Long examTypeId = examDTO.getExamTypeId();
		if (examTypeId == MOCK_TEST_A_EXAM_TYPE_ID || examTypeId == MOCK_TEST_B_EXAM_TYPE_ID || examTypeId == MOCK_TEST_FULL_EXAM_TYPE_ID) {
			return true;
		}
		
		return false;
	}
    
}
