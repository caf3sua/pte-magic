package com.vmcomms.ptemagic.service.impl;

import com.vmcomms.ptemagic.service.AnswerService;
import com.vmcomms.ptemagic.service.ExamQuestionService;
import com.vmcomms.ptemagic.service.ExamService;
import com.vmcomms.ptemagic.service.MailService;
import com.vmcomms.ptemagic.service.MarkScoreService;
import com.vmcomms.ptemagic.service.QuestionService;
import com.vmcomms.ptemagic.service.UserService;
import com.vmcomms.ptemagic.domain.Exam;
import com.vmcomms.ptemagic.domain.enumeration.ProgressType;
import com.vmcomms.ptemagic.dto.ScoreInfoDTO;
import com.vmcomms.ptemagic.repository.ExamRepository;
import com.vmcomms.ptemagic.service.dto.AnswerDTO;
import com.vmcomms.ptemagic.service.dto.ExamDTO;
import com.vmcomms.ptemagic.service.dto.ExamQuestionDTO;
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
    private QuestionService questionService;
    
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
		examDTO.setResult(ProgressType.DONE);
		examService.save(examDTO);
				
		ScoreInfoDTO scoreInfo = new ScoreInfoDTO();
		scoreInfo.setUser(userDTO);
		scoreInfo.setScore(score);
		scoreInfo.setTotalQuestion(totalScore);
		
        // Send mail
		mailService.sendScoreEmail(scoreInfo);
	}

    
}
