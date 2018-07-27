package com.vmcomms.ptemagic.service.impl;

import com.vmcomms.ptemagic.service.ConfigMockExamService;
import com.vmcomms.ptemagic.service.ExamTypeService;
import com.vmcomms.ptemagic.service.UserLimitExamService;
import com.vmcomms.ptemagic.domain.ExamType;
import com.vmcomms.ptemagic.domain.User;
import com.vmcomms.ptemagic.domain.enumeration.TestType;
import com.vmcomms.ptemagic.dto.ConfigMockExamDTO;
import com.vmcomms.ptemagic.repository.ExamTypeRepository;
import com.vmcomms.ptemagic.repository.UserRepository;
import com.vmcomms.ptemagic.security.SecurityUtils;
import com.vmcomms.ptemagic.service.dto.ExamTypeDTO;
import com.vmcomms.ptemagic.service.dto.MockExamDTO;
import com.vmcomms.ptemagic.service.mapper.ExamTypeMapper;
import com.vmcomms.ptemagic.web.rest.errors.InternalServerErrorException;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing ExamType.
 */
@Service
@Transactional
@CacheConfig(cacheNames = "exam")
public class ExamTypeServiceImpl implements ExamTypeService{

    private final Logger log = LoggerFactory.getLogger(ExamTypeServiceImpl.class);

    @Autowired
    private ExamTypeRepository examTypeRepository;

    @Autowired
    private ExamTypeMapper examTypeMapper;
    
    @Autowired
    private ConfigMockExamService configMockExamService;

    @Autowired
    private UserLimitExamService userLimitExamService;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Save a examType.
     *
     * @param examTypeDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ExamTypeDTO save(ExamTypeDTO examTypeDTO) {
        log.debug("Request to save ExamType : {}", examTypeDTO);
        ExamType examType = examTypeMapper.toEntity(examTypeDTO);
        examType = examTypeRepository.save(examType);
        return examTypeMapper.toDto(examType);
    }

    /**
     *  Get all the examTypes.
     *
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ExamTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ExamTypes");
        return examTypeRepository.findAll(pageable)
            .map(examTypeMapper::toDto);
    }

    /**
     *  Get one examType by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable
    public ExamTypeDTO findOne(Long id) {
        log.debug("Request to get ExamType : {}", id);
        ExamType examType = examTypeRepository.findOne(id);
        return examTypeMapper.toDto(examType);
    }

    /**
     *  Delete the  examType by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExamType : {}", id);
        examTypeRepository.delete(id);
    }
    
    @Cacheable
    public List<ExamTypeDTO> findAllByType(String type) {
    	log.debug("Request to findAllByType : {}", type);
        List<ExamType> data = examTypeRepository.findAllByType(TestType.valueOf(type));
        return examTypeMapper.toDto(data);
    }

	@Override
	public MockExamDTO saveMockTest(MockExamDTO mockExamDTO) {
		// calculate total question
		int totalQuestion = mockExamDTO.getLstSpeaking().size() + mockExamDTO.getLstWriting().size() 
				+ mockExamDTO.getLstReading().size() + mockExamDTO.getLstListening().size();
		mockExamDTO.getExamTypeDTO().setTotalQuestion(totalQuestion);
		
		// Save ExamTypeDTO
		ExamTypeDTO examTypeDTO = this.save(mockExamDTO.getExamTypeDTO());
		
		/** 1 - SPEAKING : cham diem 1 - 5 **/
	    /** 2 - WRITING : cham diem 6 - 7 **/
	    /** 3 - READING 8 - 12 **/
	    /** 4 - LISTENING 13 - 20 **/
		// Save ConfigMockExam SPEAKING
		for (ConfigMockExamDTO ele : mockExamDTO.getLstSpeaking()) {
			ele.setExamTypeId(examTypeDTO.getId());
			configMockExamService.save(ele);
		}
		
		// Save ConfigMockExam WRITING
		for (ConfigMockExamDTO ele : mockExamDTO.getLstWriting()) {
			ele.setExamTypeId(examTypeDTO.getId());
			configMockExamService.save(ele);
		}
		
		
		// Save ConfigMockExam READING
		for (ConfigMockExamDTO ele : mockExamDTO.getLstReading()) {
			ele.setExamTypeId(examTypeDTO.getId());
			configMockExamService.save(ele);
		}
		
		// Save ConfigMockExam LISTENING
		for (ConfigMockExamDTO ele : mockExamDTO.getLstListening()) {
			ele.setExamTypeId(examTypeDTO.getId());
			configMockExamService.save(ele);
		}
		
		// Update data
		mockExamDTO.setExamTypeDTO(examTypeDTO);
		
		return mockExamDTO;
	}

	@Override
	@Cacheable
	public List<ExamTypeDTO> getAllExamTypesByType(String type, Long userId) {
		log.debug("Request to getAllExamTypesByType : {}, userId: {}", type, userId);
        List<ExamTypeDTO> data = this.findAllByType(type);
        
        // update type incase of MOCK TEST
        if (type.contains("MOCK_TEST")) {
        	updateRemainTest(data, userId);
        }
        
        return data;
	}
	
	private void updateRemainTest(List<ExamTypeDTO> data, Long userId) {
    	for (ExamTypeDTO examTypeDTO : data) {
			// Get remain test
    		int remainTest = userLimitExamService.getRemainTest(userId, examTypeDTO.getId());
    		examTypeDTO.setRemainTest(remainTest);
		}
    }
}
