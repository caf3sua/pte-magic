package com.vmcomms.ptemagic.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vmcomms.ptemagic.domain.ConfigMockExam;
import com.vmcomms.ptemagic.dto.ConfigMockExamDTO;
import com.vmcomms.ptemagic.repository.ConfigMockExamRepository;
import com.vmcomms.ptemagic.service.ConfigMockExamService;
import com.vmcomms.ptemagic.service.ExamQuestionService;
import com.vmcomms.ptemagic.service.QuestionService;
import com.vmcomms.ptemagic.service.dto.QuestionDTO;
import com.vmcomms.ptemagic.service.mapper.ConfigMockExamMapper;


/**
 * Service Implementation for managing ConfigMockExam.
 */
@Service
@Transactional
@CacheConfig(cacheNames = "exam")
public class ConfigMockExamServiceImpl implements ConfigMockExamService {

    private final Logger log = LoggerFactory.getLogger(ConfigMockExamServiceImpl.class);

    @Autowired
    private ConfigMockExamRepository configMockExamRepository;

    @Autowired
    private ConfigMockExamMapper configMockExamMapper;
    
    @Autowired
    private QuestionService questionService;

	@Override
	@Cacheable
	public List<ConfigMockExamDTO> getMockExamByExamTypeAndGroup(Long examTypeId, String questionGroup) {
		log.debug("Request to getMockExamByExamTypeAndGroup, examTypeId: {}, questionGroup: {}", examTypeId, questionGroup);
		List<ConfigMockExam> data = configMockExamRepository.findByExamTypeIdAndQuestionGroupOrderByOrderIdAsc(examTypeId, questionGroup);
		
		return configMockExamMapper.toDto(data);
	}
	
	@Override
	@Cacheable
	public List<QuestionDTO> getMockExam(Long examTypeId, String questionGroup) {
		log.debug("Request to getMockExamByExamTypeAndGroup, examTypeId: {}, questionGroup: {}", examTypeId, questionGroup);
		List<ConfigMockExam> data = configMockExamRepository.findByExamTypeIdAndQuestionGroupOrderByOrderIdAsc(examTypeId, questionGroup);
		
		List<ConfigMockExamDTO> result = configMockExamMapper.toDto(data);
		
		// Find questionDTO
    	List<Long> ids = new ArrayList<>();
    	for (ConfigMockExamDTO configMockExamDTO : result) {
    		if (configMockExamDTO.getQuestionId() != null && configMockExamDTO.getQuestionId() > 0) {
    			ids.add(configMockExamDTO.getQuestionId());
    		}
		}
    	
    	List<QuestionDTO> listQuestionDTO = questionService.findByIdIn(ids);
    	
    	return listQuestionDTO;
	}

	@Override
	@Cacheable
	public ConfigMockExamDTO save(ConfigMockExamDTO configMockExam) {
		log.debug("Request to save, configMockExam: {}", configMockExam);
		ConfigMockExam entity = configMockExamRepository.save(configMockExamMapper.toEntity(configMockExam));
		return configMockExamMapper.toDto(entity);
	}
}

