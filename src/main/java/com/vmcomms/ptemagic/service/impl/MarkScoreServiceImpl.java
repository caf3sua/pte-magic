package com.vmcomms.ptemagic.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Acl.Role;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.vmcomms.ptemagic.domain.enumeration.ProgressType;
import com.vmcomms.ptemagic.domain.enumeration.QuestionType;
import com.vmcomms.ptemagic.domain.enumeration.TestType;
import com.vmcomms.ptemagic.dto.ScoreInfoDTO;
import com.vmcomms.ptemagic.dto.ScoreItemDTO;
import com.vmcomms.ptemagic.service.AnswerService;
import com.vmcomms.ptemagic.service.ExamQuestionService;
import com.vmcomms.ptemagic.service.ExamService;
import com.vmcomms.ptemagic.service.ExamTypeService;
import com.vmcomms.ptemagic.service.MailService;
import com.vmcomms.ptemagic.service.MarkScoreService;
import com.vmcomms.ptemagic.service.QuestionService;
import com.vmcomms.ptemagic.service.UserService;
import com.vmcomms.ptemagic.service.dto.AnswerDTO;
import com.vmcomms.ptemagic.service.dto.ExamDTO;
import com.vmcomms.ptemagic.service.dto.ExamQuestionDTO;
import com.vmcomms.ptemagic.service.dto.ExamTypeDTO;
import com.vmcomms.ptemagic.service.dto.QuestionDTO;
import com.vmcomms.ptemagic.service.dto.UserDTO;
import com.vmcomms.ptemagic.service.util.DateUtil;
import com.vmcomms.ptemagic.web.rest.errors.InternalServerErrorException;


/**
 * Service Implementation for managing Exam.
 */
@Service
@Transactional
public class MarkScoreServiceImpl implements MarkScoreService {

    private final Logger log = LoggerFactory.getLogger(MarkScoreServiceImpl.class);

    @Autowired
    private Environment env;
    
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
        
		// Update Exam -> ProgressType.DONE
		ExamDTO examDTO = examService.findOne(examId);
		
		// Check finish
		if (isFinishExam(examDTO)) {
			ExamTypeDTO examTypeDTO = examTypeService.findOne(examDTO.getExamTypeId());
			
			// Mock test part B
			if (examTypeDTO.getType().equals(TestType.MOCK_TEST_B)) {
				scoreMockTest(examId);
			} else {
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
			}
		} else {
			examDTO.setResult(ProgressType.MARKING);
			examService.save(examDTO);
		}
	}
	
	private void scoreMockTest(Long examId) {
		// User info
        UserDTO userDTO = Optional.ofNullable(userService.getUserWithAuthorities())
                .map(UserDTO::new)
                .orElseThrow(() -> new InternalServerErrorException("User could not be found"));
        
		// Update Exam -> ProgressType.DONE
		ExamDTO examDTO = examService.findOne(examId);
				
		// Score
		ScoreInfoDTO scoreInfo = new ScoreInfoDTO();
		
		// Get list exam question
		List<ExamQuestionDTO> examQuestions = examQuestionService.findAllByExamId(examId);

		ExamTypeDTO examTypeDTO = examTypeService.findOne(examDTO.getExamTypeId());
		
        // Calculate score
		if (examTypeDTO.getType().equals(TestType.MOCK_TEST_A)) {
			scoreMockTestPartA(examId, examQuestions, scoreInfo);
		} else if (examTypeDTO.getType().equals(TestType.MOCK_TEST_B)) {
			scoreMockTestPartB(examId, examQuestions, scoreInfo);
		} else {
			scoreMockTestFull(examId, examQuestions, scoreInfo);
		}
		
		
		
		scoreInfo.setUser(userDTO);
		scoreInfo.setExamTitle(examTypeDTO.getName());
		
		if (examTypeDTO.getType().equals(TestType.MOCK_TEST_A)) {
	        // Send mail
			mailService.sendScoreEmailPartA(scoreInfo);
		} else if (examTypeDTO.getType().equals(TestType.MOCK_TEST_B)) {
	        // Send mail
			mailService.sendScoreEmailPartB(scoreInfo);
		} else {
	        // Send mail
			mailService.sendScoreEmailMockTestFull(scoreInfo);
		}
			
		// Update -> DONE
		examDTO.setResult(ProgressType.DONE);
		examService.save(examDTO);
	}
	
	
	private void scoreMockTestPartB(Long examId, List<ExamQuestionDTO> examQuestions, ScoreInfoDTO scoreInfo) {
		// reading
		List<ExamQuestionDTO> fq8 = filterExamQuestion(examQuestions, QuestionType.READING_FIB_R_W);
		List<ExamQuestionDTO> fq9 = filterExamQuestion(examQuestions, QuestionType.READING_FIB_R);
		List<ExamQuestionDTO> fq10 = filterExamQuestion(examQuestions, QuestionType.READING_RE_ORDER_PARAGRAPH);
		List<ExamQuestionDTO> fq11 = filterExamQuestion(examQuestions, QuestionType.READING_MCQ_R_SINGLE_ANSWER);
		List<ExamQuestionDTO> fq12 = filterExamQuestion(examQuestions, QuestionType.READING_MCQ_R_MULTIPLE_ANSWER);
		// listening
		List<ExamQuestionDTO> fq13 = filterExamQuestion(examQuestions, QuestionType.LISTENING_SUMMARIZE_SPOKEN_TEXT);
		List<ExamQuestionDTO> fq14 = filterExamQuestion(examQuestions, QuestionType.LISTENING_FIB_L);
		List<ExamQuestionDTO> fq15 = filterExamQuestion(examQuestions, QuestionType.LISTENING_MCQ_L_SINGLE_ANSWER);
		List<ExamQuestionDTO> fq16 = filterExamQuestion(examQuestions, QuestionType.LISTENING_MCQ_L_MULTIPLE_ANSWER);
		List<ExamQuestionDTO> fq17 = filterExamQuestion(examQuestions, QuestionType.LISTENING_HIGHLIGHT_CORRECT_SUMMARY);
		List<ExamQuestionDTO> fq18 = filterExamQuestion(examQuestions, QuestionType.LISTENING_SELECT_MISSING_WORD);
		List<ExamQuestionDTO> fq19 = filterExamQuestion(examQuestions, QuestionType.LISTENING_HIGHLIGHT_INCORRECT_WORD);
		List<ExamQuestionDTO> fq20 = filterExamQuestion(examQuestions, QuestionType.LISTENING_DICTATION);
		
		// score
		ScoreItemDTO sItem8 = markScoreItem(examId, fq8);
		ScoreItemDTO sItem9 = markScoreItem(examId, fq9);
		ScoreItemDTO sItem10 = markScoreItem(examId, fq10);
		ScoreItemDTO sItem11 = markScoreItem(examId, fq11);
		ScoreItemDTO sItem12 = markScoreItem(examId, fq12);
		
		ScoreItemDTO sItem13 = markScoreItem(examId, fq13);
		ScoreItemDTO sItem14 = markScoreItem(examId, fq14);
		ScoreItemDTO sItem15 = markScoreItem(examId, fq15);
		ScoreItemDTO sItem16 = markScoreItem(examId, fq16);
		ScoreItemDTO sItem17 = markScoreItem(examId, fq17);
		ScoreItemDTO sItem18 = markScoreItem(examId, fq18);
		ScoreItemDTO sItem19 = markScoreItem(examId, fq19);
		ScoreItemDTO sItem20 = markScoreItem(examId, fq20);
		
		scoreInfo.setScoreType8(sItem8);
		scoreInfo.setScoreType9(sItem9);
		scoreInfo.setScoreType10(sItem10);
		scoreInfo.setScoreType11(sItem11);
		scoreInfo.setScoreType12(sItem12);
		
		scoreInfo.setScoreType13(sItem13);
		scoreInfo.setScoreType14(sItem14);
		scoreInfo.setScoreType15(sItem15);
		scoreInfo.setScoreType16(sItem16);
		scoreInfo.setScoreType17(sItem17);
		scoreInfo.setScoreType18(sItem18);
		scoreInfo.setScoreType19(sItem19);
		scoreInfo.setScoreType20(sItem20);
	}
	
	private void scoreMockTestPartA(Long examId, List<ExamQuestionDTO> examQuestions, ScoreInfoDTO scoreInfo) {
		
		// SPEAKING
		List<ExamQuestionDTO> fq1 = filterExamQuestion(examQuestions, QuestionType.SPEAKING_READ_ALOUD);
		List<ExamQuestionDTO> fq2 = filterExamQuestion(examQuestions, QuestionType.SPEAKING_REPEAT_SENTENCE);
		List<ExamQuestionDTO> fq3 = filterExamQuestion(examQuestions, QuestionType.SPEAKING_DESCRIBE_IMAGE);
		List<ExamQuestionDTO> fq4 = filterExamQuestion(examQuestions, QuestionType.SPEAKING_RETELL_LECTURE);
		List<ExamQuestionDTO> fq5 = filterExamQuestion(examQuestions, QuestionType.SPEAKING_ANSWER_SHORT_QUESTION);
		// WRITING
		List<ExamQuestionDTO> fq6 = filterExamQuestion(examQuestions, QuestionType.WRITING_SUMMARIZE_WRITTEN_TEXT);
		List<ExamQuestionDTO> fq7 = filterExamQuestion(examQuestions, QuestionType.WRITING_ESSAY);
		
		// score
		ScoreItemDTO sItem1 = markScoreItem(examId, fq1);
		ScoreItemDTO sItem2 = markScoreItem(examId, fq2);
		ScoreItemDTO sItem3 = markScoreItem(examId, fq3);
		ScoreItemDTO sItem4 = markScoreItem(examId, fq4);
		ScoreItemDTO sItem5 = markScoreItem(examId, fq5);
		
		ScoreItemDTO sItem6 = markScoreItem(examId, fq6);
		ScoreItemDTO sItem7 = markScoreItem(examId, fq7);
		
		scoreInfo.setScoreType8(sItem1);
		scoreInfo.setScoreType9(sItem2);
		scoreInfo.setScoreType10(sItem3);
		scoreInfo.setScoreType11(sItem4);
		scoreInfo.setScoreType12(sItem5);
		
		scoreInfo.setScoreType13(sItem6);
		scoreInfo.setScoreType14(sItem7);
	}

	private void scoreMockTestFull(Long examId, List<ExamQuestionDTO> examQuestions, ScoreInfoDTO scoreInfo) {
		scoreMockTestPartA(examId, examQuestions, scoreInfo);
		scoreMockTestPartB(examId, examQuestions, scoreInfo);
	}

	private List<ExamQuestionDTO> filterExamQuestion(List<ExamQuestionDTO> examQuestions, QuestionType questionType) {
		List<ExamQuestionDTO> filterExamQuestion = new ArrayList<>();
		
		for (ExamQuestionDTO examQuestionDTO : examQuestions) {
			// Get question to compare
			if (examQuestionDTO.getQuestionId() != null && examQuestionDTO.getQuestionId() > 0) {
				QuestionDTO questionDTO = questionService.findOne(examQuestionDTO.getQuestionId());
				// Get answer
				if (questionDTO.getType().equals(questionType)) {
					filterExamQuestion.add(examQuestionDTO);
				}
			}
		}
		
		return filterExamQuestion;
		
	}
	
	private ScoreItemDTO markScoreItem(Long examId, List<ExamQuestionDTO> examQuestions) {
		// Calculate score
		ScoreItemDTO itemScore = new ScoreItemDTO();
		itemScore.setTotalScore(examQuestions.size());
		
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
		
		itemScore.setScore(score);
		
		return itemScore;
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
		
		// Finish : MOCK_TEST_B (reading/listening)
		if (examTypeDTO.getType() == TestType.MOCK_TEST_B) {
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
    
	public String processUploadToCloud(MultipartFile file, String type) throws IOException {
		Long currentTime = System.currentTimeMillis();
		Storage storage;
		Resource resource = new ClassPathResource("config/" + env.getProperty("google-cloud.storage.credential-file"));

		storage = StorageOptions.newBuilder().setProjectId(env.getProperty("google-cloud.storage.project-id"))
				.setCredentials(ServiceAccountCredentials.fromStream(resource.getInputStream())).build().getService();

		// Create a bucket
		String bucketName = env.getProperty("google-cloud.storage.bucket-name-question");
		if (StringUtils.equals("answer", type)) {
			String prefixBucketName = env.getProperty("google-cloud.storage.bucket-name-answer-prefix"); 
			bucketName = prefixBucketName + "-" + DateUtil.currentMonth();
		}

		String filename = "";
		if (StringUtils.equals("question", type)) {
			filename = currentTime + "_" + file.getOriginalFilename();
		} else {
			filename = file.getOriginalFilename();
		}
				
		BlobId blobId = BlobId.of(bucketName, filename);

		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();

		byte[] content = file.getBytes();
		storage.create(blobInfo, content);
		storage.createAcl(blobId, Acl.of(User.ofAllUsers(), Role.READER));

		// Process resize to create thumb
		String audioUrl = "https://storage.googleapis.com/" + bucketName + "/" + filename;
		
		return audioUrl;
	}

	@Override
	public void finishExamByMarking(Long examId) {
		// User info
        UserDTO userDTO = Optional.ofNullable(userService.getUserWithAuthorities())
                .map(UserDTO::new)
                .orElseThrow(() -> new InternalServerErrorException("User could not be found"));

		// Update Exam -> ProgressType.DONE
		ExamDTO examDTO = examService.findOne(examId);
		
        ExamTypeDTO examTypeDTO = examTypeService.findOne(examDTO.getExamTypeId());
        
		// Mock test part B
		if (examTypeDTO.getType().equals(TestType.MOCK_TEST_A) || examTypeDTO.getType().equals(TestType.MOCK_TEST_FULL)) {
			scoreMockTest(examId);
		} else {
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
				
				// Calculate
				if (StringUtils.equals(answerDTO.getStatus(), "MARKING") && StringUtils.equals(answerDTO.getScore(), "OK") ) {
					score++;
				} else if (StringUtils.equals(questionDTO.getExpectAnswer(), answerDTO.getAnswer())) {
					score++;
				}
			}

			log.debug("score of user : {}/{}", score, totalScore);
			
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
		}
	}
}
