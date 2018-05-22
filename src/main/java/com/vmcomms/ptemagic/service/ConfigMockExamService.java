package com.vmcomms.ptemagic.service;

import java.util.List;

import com.vmcomms.ptemagic.dto.ConfigMockExamDTO;
import com.vmcomms.ptemagic.service.dto.QuestionDTO;

/**
 * Service Interface for managing Question.
 */
public interface ConfigMockExamService {

	List<ConfigMockExamDTO> getMockExamByExamTypeAndGroup(Long examTypeId, String questionGroup);
	
	ConfigMockExamDTO save(ConfigMockExamDTO configMockExam);
	
	List<QuestionDTO> getMockExam(Long examTypeId, String questionGroup);
}
